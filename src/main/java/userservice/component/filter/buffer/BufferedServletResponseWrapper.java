package userservice.component.filter.buffer;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class BufferedServletResponseWrapper extends HttpServletResponseWrapper {
    public BufferedServletResponseWrapper(HttpServletResponse response) {
        super(response);
    }

//    private byte[] buffer;
//
//    public BufferedServletResponseWrapper(HttpServletResponse response) throws IOException {
//        super(response);
//
//        InputStream is = response.getInputStream();
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        byte buff[] = new byte[1024];
//        int read;
//        while ((read = is.read(buff)) > 0) {
//            baos.write(buff, 0, read);
//        }
//
//        this.buffer = baos.toByteArray();
//    }
//
//    @Override
//    public ServletInputStream getInputStream() throws IOException {
//        return new BufferedServletInputStream(this.buffer);
//    }
}
