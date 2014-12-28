package edu.ntust.cs.idsl.nfctagaesdemo;

import org.ndeftools.Message;
import org.ndeftools.wellknown.TextRecord;

import android.app.Activity;
import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 * 
 */
public class TagReaderFragment extends Fragment {

    private static final String EXTRA_MESSAGE = "edu.ntust.cs.idsl.nfctagaesdemo.extra.MESSAGE";
    private static final String EXTRA_TYPE = "edu.ntust.cs.idsl.nfctagaesdemo.extra.TAG_TYPE";
    private static final String EXTRA_SIZE = "edu.ntust.cs.idsl.nfctagaesdemo.extra.MAX_SIZE";
    private MyApplication app;
    private TextView textViewTagType;
    private TextView textViewMaxSize;
    private TextView textViewCiphertext;
    private TextView textViewPlaintext;
    private ListView listViewContent;
    private Button buttonDecrypt;
    private Button buttonScan;

    public static Intent getIntentForResult(String type, int size, Message message) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_TYPE, type);
        intent.putExtra(EXTRA_SIZE, size);
        intent.putExtra(EXTRA_MESSAGE, message.getNdefMessage());
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (MyApplication) getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tag_reader, container, false);

        textViewTagType = (TextView) rootView.findViewById(R.id.textViewTagType);
        textViewMaxSize = (TextView) rootView.findViewById(R.id.textViewMaxSize);
        textViewCiphertext = (TextView) rootView.findViewById(R.id.textViewCiphertext);
        textViewPlaintext = (TextView) rootView.findViewById(R.id.textViewPlaintext);
        listViewContent = (ListView) rootView.findViewById(R.id.listViewContent);
        buttonDecrypt = (Button) rootView.findViewById(R.id.buttonDecrypt);
        buttonDecrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (app.getSettings().getKey().toString().isEmpty()) {
                    ToastMaker.toast(getActivity(), R.string.please_generate_your_key);
                    return;
                }
                if (textViewCiphertext.getText().toString().isEmpty()) {
                    ToastMaker.toast(getActivity(), R.string.ciphertext_empty);
                    return;
                }
                try {
                    byte[] key = AESCoder.base64(app.getSettings().getKey());
                    String ciphertext = textViewCiphertext.getText().toString();
                    String plaintext = AESCoder.decrypt(ciphertext, key);
                    textViewPlaintext.setText(plaintext);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        buttonScan = (Button) rootView.findViewById(R.id.buttonScan);
        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(TagReaderActivity.getIntent(getActivity()), 0);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            NdefMessage ndefMessage = (NdefMessage) data.getParcelableExtra(EXTRA_MESSAGE);
            String tagType = data.getStringExtra(EXTRA_TYPE);
            int maxSize = data.getIntExtra(EXTRA_SIZE, 0);

            Message message = null;
            try {
                message = new Message(ndefMessage);
            } catch (FormatException e) {
                e.printStackTrace();
            }
            showTagInfo(tagType, maxSize, message);
        }

        if (resultCode == Activity.RESULT_CANCELED) {
            clearTagInfo();
        }
    }

    /**
     * 
     * Show NDEF records in the list
     * 
     */
    private void showTagInfo(String tagType, int maxSize, Message message) {
        textViewTagType.setText(tagType);
        textViewMaxSize.setText(maxSize + " bytes");

        if (message != null && !message.isEmpty()) {
            ArrayAdapter<? extends Object> adapter = new NdefRecordAdapter(getActivity(), message);
            listViewContent.setAdapter(adapter);
        } else {
            clearTagInfo();
        }

        if (message.get(0) instanceof TextRecord) {
            TextRecord textRecord = (TextRecord) message.get(0);
            textViewCiphertext.setText(textRecord.getText());
            textViewPlaintext.setText("");
        }
    }

    /**
     * 
     * Clear NDEF records from list
     * 
     */
    private void clearTagInfo() {
        textViewTagType.setText("");
        textViewMaxSize.setText("");
        textViewCiphertext.setText("");
        textViewPlaintext.setText("");
        listViewContent.setAdapter(null);
    }

}
