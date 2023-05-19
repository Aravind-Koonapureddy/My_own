package com.snaplogic.snaps.Dependency;


import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.utils.SourceRoot;
import com.snaplogic.common.properties.SnapProperty;
import com.snaplogic.common.properties.builders.PropertyBuilder;
import com.snaplogic.snap.api.*;
import com.snaplogic.snap.api.capabilities.*;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@General(title = "ExceptionCheck", purpose = "check all the exception messages",
        author = "Snaplogic", docLink = "http://www.docs.com/mysnap")
@Inputs(min = 0, max = 1, accepts = {ViewType.DOCUMENT})
@Outputs(min = 1, max = 1, offers = {ViewType.DOCUMENT})
@Errors(min = 1, max = 1, offers = {ViewType.DOCUMENT})
@Version(snap = 1)
@Category(snap = SnapCategory.READ)
public class MessagesCheck extends SimpleSnap {


    String ArtifactID = "ArtifactID";
    String Path = "Path";
    private ExpressionProperty ArtifactIDExp;
    private ExpressionProperty PathExp;

    private static String POSTFIX_PATH = "/src/main/java/com/snaplogic/snaps/";
    private static Pattern pattern = Pattern.compile("\\b[A-Z0-9_]+\\b");
    private static Map<String, List<String>> map = new HashMap();

    @Override
    public void defineProperties(PropertyBuilder propertyBuilder) {

        propertyBuilder.describe(ArtifactID, "ArtifactID","ArtifactID of the dependency")
                .expression(SnapProperty.DecoratorType.ACCEPTS_SCHEMA)
                .required()
                .add();
        propertyBuilder.describe(Path,"Path","Path of the dependency")
                .expression()
                .required()
                .add();
    }

    @Override
    public void configure(PropertyValues propertyValues)  {
        ArtifactIDExp = propertyValues.getAsExpression(ArtifactID);
        PathExp = propertyValues.getAsExpression(Path);

    }

    @Override
    protected void process(Document document, String s) {
        String snappack = ArtifactIDExp.eval(document);
        String snappackPath = PathExp.eval(document);

            CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
            combinedTypeSolver.add(new ReflectionTypeSolver());
            // Configure JavaParser to use type resolution
            JavaSymbolSolver symbolSolver = new JavaSymbolSolver(combinedTypeSolver);
            StaticJavaParser.getConfiguration().setSymbolResolver(symbolSolver);

            ClassOrInterfaceDeclaration messageClazz = null;
            ArrayList messageList = null;

            /*Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the snappack artifact name:");
            String snappack = scanner.next();
            System.out.println("Enter the snappack path")
            String snappackPath = scanner.next();*/
//        snappackPath = "/home/gaian/snaplogic/Snap_v4/birst";

            // SourceRoot is a tool that read and writes Java files from packages on a certain root directory.
            // In this case the root directory is found by taking the root from the current Maven module,
            // with src/main/resources appended.
            SourceRoot sourceRoot = new SourceRoot(java.nio.file.Path.of(snappackPath));

            File dir = new File(snappackPath + POSTFIX_PATH + snappack);
            File[] directoryListing = dir.listFiles();
            Map<String, Object> map1 = new LinkedHashMap<>();

            String str ;
            /*if (directoryListing != null) {
               *//* System.out.println("\n######################## Printing Exceptions " +
                        "###########################");*//*
                for (File srcFile : directoryListing) {
                    if (srcFile.isFile()) {
                        map1.put(srcFile.getName(), "");
                        //System.out.println("\n" + srcFile.getName() + " -> ");
                        List ltr = new ArrayList();
                        messageList = new ArrayList();
                        if (srcFile.getName().equals("Messages.java")) {
                            CompilationUnit compilationUnit = null;
                            try {
                                compilationUnit = StaticJavaParser.parse(new FileInputStream(srcFile));
                            } catch (FileNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                            messageClazz =
                                    compilationUnit.getTypes().get(0).asClassOrInterfaceDeclaration();
                            continue;
                        }
                        //Reading the contents of the file
                        Scanner sc2 = null;
                        try {
                            sc2 = new Scanner(new FileInputStream(srcFile));
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                        while (sc2.hasNextLine()) {
                            String line = sc2.nextLine();
                            if (StringUtils.indexOfAny(line, "new SnapDataException",
                                    "new ConfigurationException", "new ExecutionException",
                                    "withReason", "withResolution") > 0) {
                                ltr.add(line);

                                //System.out.println("\t" + line);
                                Matcher matcher = pattern.matcher(line);
                                while (matcher.find()) {
                                    messageList.add(matcher.group());
                                }
                            }
                        }
                        map1.put(srcFile.getName(), ltr);

                    }
                    map.put(srcFile.getName(), messageList);
                }
                *//*System.out.println("\n######################## Variable Declaration " +
                        "###########################");*//*

                for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                    //System.out.println("\n" + entry.getKey() + "-> ");
                    List ltr1 = new ArrayList();
                    for (String var : entry.getValue()) {
                        try {
                            String str1;

                             str1 = getVariableType(var, messageClazz);
                             ltr1.add(str1);
                            //System.out.println("\t" + getVariableType(var, messageClazz));
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                    HashMap<String, Object> obj = new LinkedHashMap<>();
                    List errosList = (List) map1.get(entry.getKey());
                    obj.put("Exceptions ", errosList.isEmpty()?"":errosList);
                    obj.put("Variable Declaration ", ltr1.isEmpty()?"":ltr1);
                    map1.put(entry.getKey(), obj);

                }
            }*/

                if (directoryListing != null) {
                    for (File srcFile : directoryListing) {
                        if (srcFile.isFile()) {
                            System.out.println("\n" + srcFile.getName() + " -> ");
                            messageList = new ArrayList();
                            if (srcFile.getName().equals("Messages.java")) {
                                CompilationUnit compilationUnit = null;
                                try {
                                    compilationUnit = StaticJavaParser.parse(new FileInputStream(srcFile));
                                } catch (FileNotFoundException e) {
                                    throw new RuntimeException(e);
                                }
                                messageClazz =
                                        compilationUnit.getTypes().get(0).asClassOrInterfaceDeclaration();
                                continue;
                            }
                        }
                    }
                    for (File srcFile : directoryListing) {
                        if (srcFile.isFile()) {
                            System.out.println("\n"+ srcFile.getName()+" -> ");
                            messageList = new ArrayList();
                            //Reading the contents of the file
                            Scanner sc2 = null;
                            try {
                                sc2 = new Scanner(new FileInputStream(srcFile));
                            } catch (FileNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                            Map record = null;
                            while (sc2.hasNextLine()) {
                                String line = sc2.nextLine();
                                if (StringUtils.indexOfAny(line, "new SnapDataException",
                                        "new ConfigurationException", "new ExecutionException",
                                        "withReason", "withResolution") > 0) {
                                    System.out.println("\t"+line);
                                    Matcher matcher = pattern.matcher(line);
                                    while (matcher.find()) {
                                        if ( record == null) {
                                            record = new HashMap();
                                        }
                                        if (StringUtils.indexOf(line, "withResolution") > 0) {
                                            try {
                                                record.put("resolution", getVariableType(matcher.group(),
                                                        messageClazz));
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }
                                            messageList.add(record);
                                        } else if (StringUtils.indexOf(line, "withReason") > 0) {
                                            try {
                                                record.put("reason", getVariableType(matcher.group(),
                                                        messageClazz));
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }

                                        } else {
                                            record = new HashMap<String, String>();
                                            try {
                                                record.put("error", getVariableType(matcher.group(),
                                                        messageClazz));
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        map.put(srcFile.getName(), messageList);
                    }
                    // write map to output document

//            System.out.println("\n######################## Variable Declaration " +
//                    "###########################");
//            for (Entry<String, List<String>> entry : map.entrySet()) {
//                System.out.println("\n" + entry.getKey() + "-> ");
//                for (String var : entry.getValue()) {
//                    System.out.println("\t" + getVariableType(var, messageClazz));
//                }
//            }
                }


        outputViews.write(documentUtility.newDocument(map));
    }


        public static String getVariableType(String srcVariable, ClassOrInterfaceDeclaration clazz) throws Exception {
            // Find class by name
            if (clazz == null)
                throw new ClassNotFoundException();
        String value =  clazz.findAll(VariableDeclarator.class).stream()
                .filter(v -> v.getName().asString().equals(srcVariable))
                .findFirst().orElse(null)
                .getInitializer()
                .get().toString();

            /*String value = clazz.findAll(VariableDeclarator.class).stream()
                    .filter(v -> v.getName().asString().equals(srcVariable))
                    .findFirst().orElse(null).toString();*/

            return value;
        }

}
