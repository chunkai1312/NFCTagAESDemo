package edu.ntust.cs.idsl.nfctagaesdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 * 
 */
public class TagWriterFragment extends Fragment {

    private EditText editTextPlaintext;
    private TextView textViewCiphertext;
    private Button buttonEncrypt;
    private Button buttonScan;
    private MyApplication app;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (MyApplication) getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tag_writer,
                container, false);

        editTextPlaintext = (EditText) rootView.findViewById(R.id.editTextPlaintext);
        textViewCiphertext = (TextView) rootView.findViewById(R.id.textViewCiphertext);
        buttonEncrypt = (Button) rootView.findViewById(R.id.buttonEncrypt);
        buttonEncrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (app.getSettings().getKey().toString().isEmpty()) {
                    ToastMaker.toast(getActivity(), R.string.please_generate_your_key);
                    return;
                }
                if (editTextPlaintext.getText().toString().isEmpty()) {
                    ToastMaker.toast(getActivity(), R.string.plaintext_empty);
                    return;
                }
                try {
                    byte[] key = AESCoder.base64(app.getSettings().getKey());
                    String plaintext = editTextPlaintext.getText().toString();
                    String ciphertext = AESCoder.encrypt(plaintext, key);
                    textViewCiphertext.setText(ciphertext);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        buttonScan = (Button) rootView.findViewById(R.id.buttonScan);
        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textViewCiphertext.getText().toString().isEmpty())
                    ToastMaker.toast(getActivity(), R.string.ciphertext_empty);
                else
                    startActivityForResult(TagWriterActivity.getIntent(getActivity(), textViewCiphertext.getText().toString()), 0);
            }
        });

        return rootView;
    }

}
