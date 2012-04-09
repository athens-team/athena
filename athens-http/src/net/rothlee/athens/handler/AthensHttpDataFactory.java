package net.rothlee.athens.handler;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.jboss.netty.handler.codec.http.Attribute;
import org.jboss.netty.handler.codec.http.DiskFileUpload;
import org.jboss.netty.handler.codec.http.FileUpload;
import org.jboss.netty.handler.codec.http.HttpData;
import org.jboss.netty.handler.codec.http.HttpDataFactory;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.InterfaceHttpData;
import org.jboss.netty.handler.codec.http.MemoryAttribute;
import org.jboss.netty.handler.codec.http.MixedAttribute;

public class AthensHttpDataFactory implements HttpDataFactory {
	 /**
     * Proposed default MINSIZE as 16 KB.
     */
    public static long MINSIZE = 0x4000;

    private boolean checkSize = false;

    private long minSize = 0L;

    /**
     * Keep all HttpDatas until cleanAllHttpDatas() is called.
     */
    private ConcurrentHashMap<HttpRequest, List<HttpData>> requestFileDeleteMap =
        new ConcurrentHashMap<HttpRequest, List<HttpData>>();
    /**
     * HttpData will be in memory if less than default size (16KB).
     * The type will be Mixed.
     */
    public AthensHttpDataFactory() {
        checkSize = true;
        this.minSize = MINSIZE;
    }

    /**
     * HttpData will be on Disk if the size of the file is greater than minSize, else it
     * will be in memory. The type will be Mixed.
     * @param minSize
     */
    public AthensHttpDataFactory(long minSize) {
        checkSize = true;
        this.minSize = minSize;
    }

    /**
     * 
     * @param request
     * @return the associated list of Files for the request
     */
    private List<HttpData> getList(HttpRequest request) {
        List<HttpData> list = requestFileDeleteMap.get(request);
        if (list == null) {
            list = new ArrayList<HttpData>();
            requestFileDeleteMap.put(request, list);
        }
        return list;
    }
    
    @Override
    public Attribute createAttribute(HttpRequest request, String name) throws NullPointerException,
            IllegalArgumentException {
        if (checkSize) {
            Attribute attribute = new MixedAttribute(name, minSize);
            List<HttpData> fileToDelete = getList(request);
            fileToDelete.add(attribute);
            return attribute;
        }
        return new MemoryAttribute(name);
    }

    /* (non-Javadoc)
     * @see org.jboss.netty.handler.codec.http2.HttpDataFactory#createAttribute(java.lang.String, java.lang.String)
     */
    @Override
    public Attribute createAttribute(HttpRequest request, String name, String value)
            throws NullPointerException, IllegalArgumentException {
        if (checkSize) {
            Attribute attribute = new MixedAttribute(name, value, minSize);
            List<HttpData> fileToDelete = getList(request);
            fileToDelete.add(attribute);
            return attribute;
        }
        try {
            return new MemoryAttribute(name, value);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /* (non-Javadoc)
     * @see org.jboss.netty.handler.codec.http2.HttpDataFactory#createFileUpload(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public FileUpload createFileUpload(HttpRequest request, String name, String filename,
            String contentType, String contentTransferEncoding, Charset charset,
            long size) throws NullPointerException, IllegalArgumentException {
            FileUpload fileUpload = new DiskFileUpload(name, filename, contentType,
                    contentTransferEncoding, charset, size);
            List<HttpData> fileToDelete = getList(request);
            fileToDelete.add(fileUpload);
            return fileUpload;
    }

    @Override
    public void removeHttpDataFromClean(HttpRequest request, InterfaceHttpData data) {
        if (data instanceof HttpData) {
            List<HttpData> fileToDelete = getList(request);
            fileToDelete.remove(data);
        }
    }

    @Override
    public void cleanRequestHttpDatas(HttpRequest request) {
        List<HttpData> fileToDelete = requestFileDeleteMap.remove(request);
        if (fileToDelete != null) {
            for (HttpData data: fileToDelete) {
                data.delete();
            }
            fileToDelete.clear();
        }
    }

    @Override
    public void cleanAllHttpDatas() {
        for (HttpRequest request : requestFileDeleteMap.keySet()) {
            List<HttpData> fileToDelete = requestFileDeleteMap.get(request);
            if (fileToDelete != null) {
                for (HttpData data: fileToDelete) {
                    data.delete();
                }
                fileToDelete.clear();
            }
            requestFileDeleteMap.remove(request);
        }
    }
}
