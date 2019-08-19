package com.example.pageturner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

public class Play extends AppCompatActivity {
    Drawable[] images = pictures.getData();
    List<String> labels = new ArrayList<String>();
    private static final String LABEL_FILENAME = "file:///android_asset/labels.txt";
    private static final String MODEL_FILENAME = "file:///android_asset/model_18000.pb";
    private static final int SAMPLE_RATE = 16000;
    private static final int SAMPLE_DURATION_MS = 1000;
    private static final int RECORDING_LENGTH = (int) (SAMPLE_RATE * SAMPLE_DURATION_MS / 1000);
    boolean shouldContinue = true;
    private Thread recordingThread;
    short[] recordingBuffer = new short[RECORDING_LENGTH];
    int recordingOffset = 0;
    private final ReentrantLock recordingBufferLock = new ReentrantLock();
    private static final int REQUEST_RECORD_AUDIO = 13;
    private static final String LOG_TAG = Play.class.getSimpleName();
    private TensorFlowInferenceInterface inferenceInterface;
    boolean shouldContinueRecognition = true;
    private Thread recognitionThread;
    private static final String OUTPUT_SCORES_NAME = "labels_softmax";
    private static final String SAMPLE_RATE_NAME = "decoded_sample_data:1";
    private static final String INPUT_DATA_NAME = "decoded_sample_data:0";
    private static final long MINIMUM_TIME_BETWEEN_SAMPLES_MS = 30;
    private RecognizeCommands recognizeCommands = null;
    private static final long AVERAGE_WINDOW_DURATION_MS = 500;
    private static final float DETECTION_THRESHOLD = 0.70f;
    private static final int SUPPRESSION_MS = 1500;
    private static final int MINIMUM_COUNT = 3;
    public int current_image = 0;
    public TextView modelTest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        modelTest = (TextView)findViewById(R.id.modelTest);
        ImageView im = (ImageView) findViewById(R.id.image);
        im.setImageDrawable(images[0]);
        BufferedReader br = null;
        try{
            br = new BufferedReader(new InputStreamReader(getAssets().open("labels.txt")));
            String line;
            while ((line = br.readLine()) != null) {
                labels.add(line);
                System.out.println(line);
            }
            br.close();
        }catch (IOException e){
            e.printStackTrace();
        }

        recognizeCommands =
                new RecognizeCommands(
                        labels,
                        AVERAGE_WINDOW_DURATION_MS,
                        DETECTION_THRESHOLD,
                        SUPPRESSION_MS,
                        MINIMUM_COUNT,
                        MINIMUM_TIME_BETWEEN_SAMPLES_MS);

        inferenceInterface = new TensorFlowInferenceInterface(getAssets(), MODEL_FILENAME);
        //modelTest.setText("inferenceInterface loaded");
        requestMicrophonePermission();
        //modelTest.setText("start record function");
        startRecording();
        //modelTest.setText("start recognizing");
        startRecognition();

    }

    private void requestMicrophonePermission() {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion > android.os.Build.VERSION_CODES.LOLLIPOP){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
                }
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_RECORD_AUDIO
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startRecording();
            startRecognition();
        }
    }

    public synchronized void startRecording(){
        if(recordingThread != null){
            return;
        }
        shouldContinue = true;
        recordingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                record();
            }
        });
        recordingThread.start();
    }
    public synchronized void stopRecording() {
        if (recordingThread == null) {
            return;
        }
        shouldContinue = false;
        recordingThread = null;
    }
    private void record(){
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_AUDIO);
        int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        if(bufferSize == AudioRecord.ERROR || bufferSize == AudioRecord.ERROR_BAD_VALUE){
            bufferSize = SAMPLE_RATE * 2;
        }
        short[] audioBuffer = new short[bufferSize/2];

        AudioRecord record =
                new AudioRecord(
                        MediaRecorder.AudioSource.DEFAULT,
                        SAMPLE_RATE,
                        AudioFormat.CHANNEL_IN_MONO,
                        AudioFormat.ENCODING_PCM_16BIT,
                        bufferSize);
        if (record.getState() != AudioRecord.STATE_INITIALIZED) {
            Log.e(LOG_TAG, "Audio Record can't initialize!");
            return;
        }

        record.startRecording();
        modelTest.setText("start recording");
        while(shouldContinue){
            int numberRead = record.read(audioBuffer,0,audioBuffer.length);
            int maxLength = recordingBuffer.length;
            int newRecordingOffset = recordingOffset + numberRead;
            int secondCopyLength = Math.max(0, newRecordingOffset - maxLength);
            int firstCopyLength = numberRead - secondCopyLength;
            recordingBufferLock.lock();
            try{
                System.arraycopy(audioBuffer, 0, recordingBuffer, recordingOffset, firstCopyLength);
                System.arraycopy(audioBuffer, firstCopyLength, recordingBuffer, 0, secondCopyLength);
                recordingOffset = newRecordingOffset % maxLength;
            }finally {
                recordingBufferLock.unlock();
            }
        }
        record.stop();
        record.release();

    }
    public synchronized void startRecognition() {
        if (recognitionThread != null) {
            return;
        }
        shouldContinueRecognition = true;
        recognitionThread =
                new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                                recognize();
                            }
                        });
        recognitionThread.start();
    }

    public synchronized void stopRecognition() {
        if (recognitionThread == null) {
            return;
        }
        shouldContinueRecognition = false;
        recognitionThread = null;
    }

    private void recognize() {
        Log.v(LOG_TAG, "Start recognition");
        //modelTest.setText("start recognition");
        short[] inputBuffer = new short[RECORDING_LENGTH];
        float[] floatInputBuffer = new float[RECORDING_LENGTH];
        final float[] outputScores = new float[labels.size()];
        String[] outputScoresNames = new String[] {OUTPUT_SCORES_NAME};
        int[] sampleRateList = new int[] {SAMPLE_RATE};

        // Loop, grabbing recorded data and running the recognition model on it.
        while (shouldContinueRecognition) {
            // The recording thread places data in this round-robin buffer, so lock to
            // make sure there's no writing happening and then copy it to our own
            // local version.
            recordingBufferLock.lock();
            try {
                int maxLength = recordingBuffer.length;
                int firstCopyLength = maxLength - recordingOffset;
                int secondCopyLength = recordingOffset;
                System.arraycopy(recordingBuffer, recordingOffset, inputBuffer, 0, firstCopyLength);
                System.arraycopy(recordingBuffer, 0, inputBuffer, firstCopyLength, secondCopyLength);
            } finally {
                recordingBufferLock.unlock();
            }

            // We need to feed in float values between -1.0f and 1.0f, so divide the
            // signed 16-bit inputs.
            for (int i = 0; i < RECORDING_LENGTH; ++i) {
                floatInputBuffer[i] = inputBuffer[i] / 32767.0f;
            }

            // Run the model.
            //modelTest.setText("start running the model");
            inferenceInterface.feed(SAMPLE_RATE_NAME, sampleRateList);
            inferenceInterface.feed(INPUT_DATA_NAME, floatInputBuffer, RECORDING_LENGTH, 1);
            inferenceInterface.run(outputScoresNames);
            inferenceInterface.fetch(OUTPUT_SCORES_NAME, outputScores);
            long currentTime = System.currentTimeMillis();
            final RecognizeCommands.RecognitionResult result = recognizeCommands.processLatestResults(outputScores,currentTime);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ImageView im = (ImageView) findViewById(R.id.image);
                    String newLabels[] = new String[labels.size()];
                    for (int j = 0; j < labels.size(); j++) {
                        // Assign each value to String array
                        newLabels[j] = labels.get(j);
                    }
                    //modelTest.setText("run through smoother");
                    if(!result.foundCommand.startsWith("_") && result.isNewCommand){
                        int labelIndex = -1;
                        for(int j = 0;j<labels.size();j++){
                            if(labels.get(j).equals(result.foundCommand)){
                                labelIndex = j;
                                modelTest.setText(newLabels[labelIndex]+" "+outputScores[labelIndex]);
                                if(newLabels[labelIndex]==newLabels[11] && current_image!=images.length-1){
                                    current_image++;
                                    im.setImageDrawable(images[current_image]);
                                }
                                //check if command is stop
                                if(newLabels[labelIndex]==newLabels[10] && current_image!=0){
                                    current_image = current_image-1;
                                    im.setImageDrawable(images[current_image]);
                                }
                            }
                        }
                    }

                }
            });




            try {
                // We don't need to run too frequently, so snooze for a bit.
                Thread.sleep(MINIMUM_TIME_BETWEEN_SAMPLES_MS);
            } catch (InterruptedException e) {
                // Ignore
            }
        }

        Log.v(LOG_TAG, "End recognition");
    }

}
