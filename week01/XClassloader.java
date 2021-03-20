package geek.week01;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class XClassloader extends ClassLoader {
	
	//指定class路径
	private String classPath;
	
	/**
	 * 
	* @Title: XClassloader
	* @Description: XClassloader构造函数
	* @param classPath class路径
	 */
	public XClassloader(final  String classPath) {
		this.classPath=classPath;
	}

	/**
	 *
	* @param name 类名（全路径）
	* @return 
	* @throws ClassNotFoundException
	* @see java.lang.ClassLoader#findClass(java.lang.String)
	 */
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		 byte[] classBytes=getClassData();
		 
		 if(classBytes==null) {
			 throw new ClassNotFoundException();
		 }
		 decodeClass(classBytes);
		 return defineClass(name, classBytes, 0, classBytes.length);
	}
	
	
	
	/**
	 * 
	* @Title: getClassData
	* @Description: 根据指定路径，获取class文件字节数组
	* @return class文件字节数组
	 */
	private  byte[] getClassData() {
		File file = new File(classPath);
        if (file.exists()){
            FileInputStream in = null;
            ByteArrayOutputStream out = null;
            try {
                in = new FileInputStream(file);
                out = new ByteArrayOutputStream();
 
                byte[] buffer = new byte[1024];
                int size = 0;
                while ((size = in.read(buffer)) != -1) {
                    out.write(buffer, 0, size);
                }
 
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            	 if (in != null) {
                     try {
                    	 in.close();
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                 }
                 if (out != null) {
                     try {
                    	 out.close();
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                 }
            }
            return out.toByteArray();
        }else{
            return null;
        }

	}
	
	/**
	 * 
	* @Title: decodeClass
	* @Description: 解码xclass
	* @param classBytes
	 */
	private void decodeClass(byte[] classBytes) {
		for(int i=0;i<classBytes.length;i++) {
			 classBytes[i]=(byte)(255-classBytes[i] );
		 }
	}
	
	public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		String classPath="E:/geeklearning/week01/Hello.xlass";
		final XClassloader myClassloader=new XClassloader(classPath);
		String packageNamePath="Hello";
		Class<?> helloClass=myClassloader.findClass(packageNamePath);
		Method method = helloClass.getDeclaredMethod("hello");
		Object object = helloClass.newInstance();
        method.invoke(object);
	}
	
	

}
