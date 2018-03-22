package com.example.nodav.cryptoreview.network;


import com.example.nodav.cryptoreview.R;

import java.io.IOException;


public class NetworkError extends Throwable {

    private final Throwable error;

    public NetworkError(Throwable e) {
        super(e);
        this.error = e;
    }

    public int getStringErrorId() {
        if (this.error instanceof IOException)
            return R.string.no_connection_error_message;
        else
            return R.string.default_error_message;
    }

}
