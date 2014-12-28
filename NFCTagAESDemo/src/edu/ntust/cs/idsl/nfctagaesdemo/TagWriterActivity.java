package edu.ntust.cs.idsl.nfctagaesdemo;

import java.nio.charset.Charset;
import java.util.Locale;

import org.ndeftools.Message;
import org.ndeftools.util.activity.NfcTagWriterActivity;
import org.ndeftools.wellknown.TextRecord;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class TagWriterActivity extends NfcTagWriterActivity {

	private static final String ACTION = "edu.ntust.cs.idsl.nfctagaesdemo.action.WRITE_TAG";
	private static final String EXTRA = "edu.ntust.cs.idsl.nfctagaesdemo.extra.TEXT";
	private String text;
	private Button cancelButton;
	
	public static Intent getIntent(Context context, String text) {
		Intent intent = new Intent(context, TagWriterActivity.class);
		intent.setAction(ACTION);
		intent.putExtra(EXTRA, text);
		return intent;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		setContentView(R.layout.activity_tag_writer);
		text = getIntent().getStringExtra(EXTRA);	
		setDetecting(true);
		
		cancelButton = (Button) findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();     
            }
        });
	}
	
    @Override
    public void nfcIntentDetected(Intent intent, String action) {
        super.nfcIntentDetected(intent, action);
        vibrate(200);
    }  
	
	/**
	 * 
	 * Create an NDEF message to be written when a tag is within range.
	 * 
	 * @return the message to be written
	 */
	@Override
	protected NdefMessage createNdefMessage() {
		Message message = new Message();
		
		TextRecord textRecord = new TextRecord();
		textRecord.setText(text);
		textRecord.setEncoding(Charset.forName("UTF-8"));
		textRecord.setLocale(Locale.TAIWAN);
		message.add(textRecord);
		
		return message.getNdefMessage();
	}
	
	/**
	 * 
	 * Writing NDEF message to tag failed.
	 * 
	 * @param e exception
	 */
	@Override
	protected void writeNdefFailed(Exception e) {
	    ToastMaker.toast(this, getString(R.string.ndefWriteFailed, e.toString()));
        setResult(Activity.RESULT_CANCELED);
		finish();
	}

	/**
	 * 
	 * Tag is not writable or write-protected.
	 * 
	 * @param e exception
	 */
	@Override
	public void writeNdefNotWritable() {
	    ToastMaker.toast(this, R.string.tagNotWritable);
        setResult(Activity.RESULT_CANCELED);
		finish();
	}

	/**
	 * 
	 * Tag capacity is lower than NDEF message size.
	 * 
	 * @param e exception
	 */
	@Override
	public void writeNdefTooSmall(int required, int capacity) {
	    ToastMaker.toast(this, getString(R.string.tagTooSmallMessage,  required, capacity));
        setResult(Activity.RESULT_CANCELED);
		finish();
	}


	/**
	 * 
	 * Unable to write this type of tag.
	 * 
	 */
	@Override
	public void writeNdefCannotWriteTech() {
	    ToastMaker.toast(this, R.string.cannotWriteTechMessage);
        setResult(Activity.RESULT_CANCELED);
		finish();
	}

	/**
	 * 
	 * Successfully wrote NDEF message to tag.
	 * 
	 */
	@Override
	protected void writeNdefSuccess() {
	    ToastMaker.toast(this, R.string.ndefWriteSuccess);
        setResult(Activity.RESULT_OK);
		finish();
	}
   /**
     * 
     * NFC feature was found and is currently enabled
     * 
     */
	
	@Override
	protected void onNfcStateEnabled() {
//		toast(getString(R.string.nfcAvailableEnabled));
	}

    /**
     * 
     * NFC feature was found but is currently disabled
     * 
     */

	@Override
	protected void onNfcStateDisabled() {
	    ToastMaker.toast(this, R.string.nfcAvailableDisabled);
		startNfcSettingsActivity();
		setResult(Activity.RESULT_CANCELED);
		finish();
	}

	/**
     * 
     * NFC setting changed since last check. For example, the user enabled NFC in the wireless settings.
     * 
     */

	@Override
	protected void onNfcStateChange(boolean enabled) {
		if(enabled) {
		    ToastMaker.toast(this, R.string.nfcSettingEnabled);
		} else {
		    ToastMaker.toast(this, R.string.nfcSettingDisabled);
		}
	}

	/**
	 * 
	 * This device does not have NFC hardware
	 * 
	 */
	
	@Override
	protected void onNfcFeatureNotFound() {
	    ToastMaker.toast(this, R.string.noNfcMessage);
		setResult(Activity.RESULT_CANCELED);
		finish();
	}
	
    public void vibrate(int duration) {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(duration);
    }

}
