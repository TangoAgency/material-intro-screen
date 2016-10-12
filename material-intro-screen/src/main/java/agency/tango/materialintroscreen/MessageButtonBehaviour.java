package agency.tango.materialintroscreen;

import android.view.View;

@SuppressWarnings("unused")
public class MessageButtonBehaviour {
    private View.OnClickListener clickListener;
    private String messageButtonText;

    public MessageButtonBehaviour(View.OnClickListener clickListener, String messageButtonText) {
        this.clickListener = clickListener;
        this.messageButtonText = messageButtonText;
    }

    public MessageButtonBehaviour(String messageButtonText) {
        this.messageButtonText = messageButtonText;
    }

    public View.OnClickListener getClickListener() {
        return clickListener;
    }

    public String getMessageButtonText() {
        return messageButtonText;
    }
}
