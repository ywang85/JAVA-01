import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;

public class MyClassLoader extends ClassLoader {

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] bytes = null;
        try {
            URL url = MyClassLoader.class.getResource("Hello.xlass");
            String filePath = url.getPath();
            bytes = inputStream2ByteArray(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return defineClass(name, bytes, 0, bytes.length);
    }

    private byte[] inputStream2ByteArray(String filePath) throws IOException {
        InputStream in = new FileInputStream(filePath);
        byte[] data = toByteArray(in);
        in.close();
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) (255 - data[i]);
        }
        return data;
    }

    private byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        MyClassLoader myClassLoader = new MyClassLoader();
        Class<?> helloClass = myClassLoader.findClass("Hello");
        Object o = helloClass.newInstance();
        Method method = helloClass.getDeclaredMethod("hello", null);
        method.invoke(o, null);
    }
}
