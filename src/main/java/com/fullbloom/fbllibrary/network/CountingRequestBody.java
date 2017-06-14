package com.fullbloom.fbllibrary.network;


import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * 作者：zwq on 2016/7/13 11:22
 */
public class CountingRequestBody extends RequestBody {

    protected RequestBody delegate;
    protected OnResponseListener listener;

    protected CountingSink countingSink;
    private int key;
    public CountingRequestBody(int key , RequestBody delegate, OnResponseListener listener)
    {
        this.delegate = delegate;
        this.listener = listener;
        this.key = key;
    }

    @Override
    public MediaType contentType()
    {
        return delegate.contentType();
    }

    @Override
    public long contentLength()
    {
        try
        {
            return delegate.contentLength();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException
    {

        countingSink = new CountingSink(sink);
        BufferedSink bufferedSink = Okio.buffer(countingSink);

        delegate.writeTo(bufferedSink);

        bufferedSink.flush();
    }

    protected final class CountingSink extends ForwardingSink
    {
        private long bytesWritten = 0;

        public CountingSink(Sink delegate)
        {
            super(delegate);
        }

        @Override
        public void write(Buffer source, long byteCount) throws IOException
        {
            super.write(source, byteCount);
            bytesWritten += byteCount;
            int x = (int)bytesWritten;
            int y = (int)contentLength();
            float z = (float)x/y;
            int a = Math.round(z *100);
            listener.onOkRequestProgress(key,bytesWritten, contentLength(),a);
        }
    }
}
