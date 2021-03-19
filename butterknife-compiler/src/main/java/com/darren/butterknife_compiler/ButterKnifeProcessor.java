package com.darren.butterknife_compiler;

import com.darren.butterknife_annotation.BindView;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

@AutoService(Processor.class)
public class ButterKnifeProcessor extends AbstractProcessor {
    //文件
    Filer filer;
    //节点工具类
    Elements elements;

    static final String superClassPackage = "com.darren.butterknife";
    static final String unBinder = "UnBinder";
    static final String util = "Utils";

    //初始化工具
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        filer = processingEnv.getFiler();
        elements = processingEnv.getElementUtils();
    }

    //设置支持的注解
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new HashSet<>();
        types.add(BindView.class.getCanonicalName());
        return types;
    }

    //支持的版本
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return processingEnv.getSourceVersion();
    }

    //核心方法
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        //获取当前源码中所有使用 @BindView 的变量节点
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(BindView.class);
        //存储每个 Activity 对应的节点
        Map<TypeElement, List<VariableElement>> map = new HashMap<>();
        //便利 Set 集合
        for (Element element : elements) {
            //变量节点
            VariableElement variableElement = (VariableElement) element;
            //类节点
            TypeElement typeElement = (TypeElement) variableElement.getEnclosingElement();
            //根据类节点获取属性节点
            List<VariableElement> variableElementList = map.get(typeElement);
            //判断当前的类节点对应的属性节点集合
            if (variableElementList == null) {
                variableElementList = new ArrayList<>();
                map.put(typeElement, variableElementList);
            }
            variableElementList.add(variableElement);
        }


        //便利 Activity 中的节点，通过 JavaPoet 生成 Java 文件
        for (Map.Entry<TypeElement, List<VariableElement>> entry : map.entrySet()) {
            //获取类节点
            TypeElement typeElement = entry.getKey();
            //获取变量节点列表
            List<VariableElement> variableElements = entry.getValue();
            //获取类名
            String activityName = getClassName(typeElement);
            //获取包名
            String packageName = getPackageName(typeElement);
            //父类
            ClassName superClass = ClassName.get(superClassPackage, unBinder);
            //自己
            ClassName className = ClassName.bestGuess(activityName);
            //创建类并继承UnBinder
            TypeSpec.Builder classBuilder = TypeSpec.classBuilder(activityName + "_ViewBinding")
                    .addModifiers(Modifier.PUBLIC)
                    .addSuperinterface(superClass)
                    .addField(className, "target", Modifier.PRIVATE);

            //创建 unbind 方法
            MethodSpec.Builder unbindBuilder = MethodSpec.methodBuilder("unbind")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC);
            unbindBuilder.addStatement("$T target = this.target", className);
            unbindBuilder.addStatement("if (target == null) throw new IllegalStateException(\"Bindings already cleared.\")");
            unbindBuilder.addStatement("this.target = null");

            //创建构造方法
            ClassName uiThreadClassName = ClassName.get("androidx.annotation", "UiThread");
            MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                    .addAnnotation(uiThreadClassName)
                    .addParameter(className, "target", Modifier.FINAL)
                    .addModifiers(Modifier.PUBLIC);
            constructorBuilder.addStatement("this.target = target");

            ClassName utilClass = ClassName.get(superClassPackage, util);
            //便利变量集合，在构造方法中完成 findViewById 逻辑
            for (VariableElement variableElement : variableElements) {
                //通过注解拿到 id
                int id = variableElement.getAnnotation(BindView.class).value();
                //获取变量名
                String fileName = variableElement.getSimpleName().toString();
                //$L for Literals 替换字符串
                //$T for Types 替换类型,可以理解成对象
                constructorBuilder.addStatement("target.$L = $T.findViewById(target,$L,$T.class)", fileName, utilClass, id,variableElement.asType());
                unbindBuilder.addStatement("target.$L = null", fileName);
            }

            //添加方法
            classBuilder.addMethod(unbindBuilder.build());
            classBuilder.addMethod(constructorBuilder.build());

            //将 Java 写成 Class 文件
            try {
                JavaFile.builder(packageName, classBuilder.build())
                        .addFileComment("ButterKnifeProcessor")
                        .build()
                        .writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    private String getClassName(TypeElement typeElement) {
        String className = typeElement.getSimpleName().toString();
        return className;
    }

    private String getPackageName(TypeElement typeElement) {
        String packageName = elements.getPackageOf(typeElement).toString();
        return packageName;
    }
}