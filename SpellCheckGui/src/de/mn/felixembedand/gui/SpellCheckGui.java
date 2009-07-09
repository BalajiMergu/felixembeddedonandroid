package de.mn.felixembedand.gui;

// interface defined in an other bundle, has to be imported as jar file
// so that eclipse knows this interface
// to be save, that its there on runtime (this project here is not a android project, so including jars is not given)
// we have this interface providing jar as an (ipojo)bundle in /bundle/ dir

import spell.services.SpellChecker;

// same situation, hostapp defines this interface, in order to let SpellCheckGui know about this interface
// we have to import it
import de.mn.felixembedand.view.ViewFactory;



import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SpellCheckGui implements ViewFactory{
	
    private SpellChecker checker;
    private LinearLayout m_main;
    private Button m_button;
    private EditText m_text;
    private TextView m_result;
    private Activity activity;
    
    public SpellCheckGui() {
        System.out.println("Spell checker GUI created ...");
    }

    public View create(Activity activity) {
        this.activity = activity;
        
        System.out.println("=== Create the view");
        m_main = new LinearLayout(activity);
        m_main.setOrientation(LinearLayout.VERTICAL);
        
        m_text = new EditText(activity);
        m_text.setLines(1);
        
        m_button = new Button(activity);
        m_button.setText("check");
        m_button.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                String[] array = checker.check(m_text.getText().toString());
                if (array != null) {
                    m_result.setText(array.length + " words mispelled");
                } else {
                    m_result.setText("No mispelled words");
                }
            }
            
        });
        
        synchronized(this) {
            if (checker != null) {
                m_button.setEnabled(true);
            } else {
                m_button.setEnabled(false);
            }
        }
        
        m_result = new TextView(activity);
        
        m_main.addView(m_text);
        m_main.addView(m_button);
        m_main.addView(m_result);
        m_main.setVisibility(View.VISIBLE);
        return m_main;
        
    }
    
    
    public synchronized void bindSpellChecker(SpellChecker sp) {
        checker = sp;
        
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    // Enable button
                    m_button.setEnabled(true);
                    m_result.setText("Enter words, and click on 'check'");
                    m_main.invalidate();
                    System.out
                            .println("=====> Spell checker GUI receives a new spell checker ...");
                }
            });
        }
    }
    
    public synchronized void unbindSpellChecker(SpellChecker sp) {
        checker = null;
        if (activity != null) {
        activity.runOnUiThread(new Runnable() {
                public void run() {
                    // Enable button
                    m_button.setEnabled(false);
                    m_result.setText("No Spellchecker available");
                    m_main.invalidate();
                    System.out
                            .println("=====> Spell checker GUI was unbind from the spell checker");
                }
            });
        }

    }


}
