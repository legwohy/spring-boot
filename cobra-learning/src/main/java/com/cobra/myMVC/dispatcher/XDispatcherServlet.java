package com.cobra.myMVC.dispatcher;

import com.cobra.myMVC.annotation.*;
import com.cobra.myMVC.controller.XUserController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * servlet3.1 支持注解 替代web.xml
 */
@WebServlet(name = "xDispatcherServlet",
        urlPatterns = "/*",
        loadOnStartup = 1,
        initParams = {@WebInitParam(name = "base-package",value = "com.cobra.myMVC")})
public class XDispatcherServlet extends HttpServlet {
    private String basePackage = "";

    /** 全类名*/
    private List<String> packageNames = new ArrayList<>();

    /** 注解实例化 注解上的类名称:实例化对象 类名:实例化对象*/
    private Map<String,Object> instanceMap = new HashMap<>();

    /** 全类名：类名*/
    private Map<String,String> nameMap = new HashMap<>();

    /** url:method*/
    private Map<String,Method> urlMethodMap = new HashMap<>();

    /** method:全类名  反射调用*/
    private Map<Method,String> methodPackageMap = new HashMap<>();


    @Override
    public void init(ServletConfig config) throws ServletException {
        basePackage = config.getInitParameter("base-package");
       try {
           // 1、扫描基包得到全部的带包路径全限定名 放在集合 packageNames 中
           scanBasePackage(basePackage);

           // 2、实例化带注解的类
           instance(packageNames);

           // 3、springIOC依赖注入
           springIOC();

           // 4、url与方法映射
           handlerUrlMethodMap();
       }catch (Exception e){

       }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        String path = url.replaceAll(contextPath,url);
        Method method = urlMethodMap.get(path);
        if(null != method){
            // 激活方法
            String packageName = methodPackageMap.get(method);
            String controllerName = nameMap.get(packageName);

            XUserController obj = (XUserController)instanceMap.get(controllerName);

            try {
                method.setAccessible(true);
                method.invoke(obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private void handlerUrlMethodMap() throws ClassNotFoundException {
        if(packageNames.isEmpty()){
            return;
        }
        for (String packageName: packageNames){
            Class clazz = Class.forName(packageName);
            // 扫描controller下的方法
            if(clazz.isAnnotationPresent(XController.class)){

                StringBuffer baseUrl = new StringBuffer();

                // 类上的路径
                if(clazz.isAnnotationPresent(XRequestMapping.class)){
                    XRequestMapping requestMapping = (XRequestMapping) clazz.getAnnotation(XRequestMapping.class);
                    baseUrl.append(requestMapping.value());
                }

                // 方法上的路径
                Method[] methods = clazz.getMethods();
                for (Method method : methods){
                    if(method.isAnnotationPresent(XRequestMapping.class)){
                        XRequestMapping requestMapping = (XRequestMapping) method.getAnnotation(XRequestMapping.class);
                        baseUrl.append(requestMapping.value());

                        urlMethodMap.put(baseUrl.toString(),method);
                        methodPackageMap.put(method,packageName);
                    }
                }
            }


        }
    }

    private void springIOC() throws IllegalAccessException {
        for (Map.Entry<String,Object> en: instanceMap.entrySet()){
            // 获取类中的属性
            Field[] fields = en.getValue().getClass().getDeclaredFields();
            for (Field field : fields){
                // 只争对带有 XQualifier 注解的
                if(field.isAnnotationPresent(XQualifier.class)){
                    String name = field.getAnnotation(XQualifier.class).value();
                    field.setAccessible(true);

                    // set(原对象，值)
                    field.set(en.getValue(),instanceMap.get(name));
                }
            }
        }
    }

    /** 类上的注解 只争对方在类上的 XController、XService、XRepository*/
    private void instance(List<String> packageNames) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        if(packageNames.isEmpty()){
            return;
        }
        for (String packageName: packageNames){
            Class clazz = Class.forName(packageName);
            // 判断是否注解
            if(clazz.isAnnotationPresent(XController.class)){
                XController controller = (XController) clazz.getAnnotation(XController.class);
                String controllerName = controller.value();
                instanceMap.put(controllerName,clazz.newInstance());
                nameMap.put(packageName,controllerName);

            }else  if(clazz.isAnnotationPresent(XService.class)){
                XService service = (XService) clazz.getAnnotation(XService.class);
                String serviceName = service.value();
                instanceMap.put(serviceName,clazz.newInstance());
                nameMap.put(packageName,serviceName);

            }else  if(clazz.isAnnotationPresent(XRepository.class)){
                XRepository repository = (XRepository) clazz.getAnnotation(XRepository.class);
                String repositoryName = repository.value();
                instanceMap.put(repositoryName,clazz.newInstance());
                nameMap.put(packageName,repositoryName);
            }
        }
    }

    /**
     *
     * com.cobra.myMVC 下面的所有类
     * com.cobra.myMVC
     *               .service
     *               .dao
     *
     */
    private void scanBasePackage(String basePackage) {
        URL url = this.getClass().getClassLoader()
                .getResource(basePackage.replaceAll("\\.","/"));

        File basePackageFile = new File(url.getPath());

        // 遍历文件
        File[] childrenFile = basePackageFile.listFiles();
        for (File file : childrenFile){
            if(file.isDirectory()){
                // 目录 继续往下查找 com.cobra.myMVC.service
                scanBasePackage(basePackage+"."+file.getName());
            }else if(file.isFile()){
                // 只读取到名字 com.cobra.myMVC.service.UserServiceImpl.class 去掉class
                packageNames.add(basePackage+"."+file.getName().split("\\.")[0]);
            }
        }

    }
}
