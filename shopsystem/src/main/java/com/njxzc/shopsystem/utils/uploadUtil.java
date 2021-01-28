package com.njxzc.shopsystem.utils;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.UUID;

public class uploadUtil {
    /*实现上传单个文件可以指定上传地址，最终上传成功后返回该文件实际访问地址*/
    /*MultipartFile file:获取上传文件*/
    /*String uploadPath：指定上传的地址*/
    /*HttpServletRequest request:获取上下文路径*/
    /*String type:目的是区分 到底是commodity 还是user 或者是其他文件结构*/
    public static String uploadOneFile(MultipartFile file, String uploadPath, HttpServletRequest request,String type){
        String flag="";
        if(file.isEmpty()){
            System.out.println("没有获取到文件");
            return "";
        }else {
            String fileName = file.getOriginalFilename();
            System.out.println("文件名："+fileName);
            String suffixName = fileName.substring(fileName.lastIndexOf("."));//获取文件后缀
            //根据不同后缀 将不同类型文件上传到不同文件夹（前端控制）
            System.out.println("文件后缀："+suffixName);
            //文件保存地址 最后一定要添加//
            //文件名重构 考虑到文件同名的问题，这里最好将文件名通过UUID重新处理
            fileName = UUID.randomUUID()+suffixName;//这样可以保证生成的文件名 不重复
            System.out.println("重构后文件名："+fileName);
            String uploadFileUrl = uploadPath;
            //如果是图片后缀可以是.jpeg .jpg .gif .png 如果还有其他类型
            if(suffixName.equalsIgnoreCase(".jpg")||suffixName.equalsIgnoreCase(".png")
                    ||suffixName.equalsIgnoreCase(".jpeg")||suffixName.equalsIgnoreCase(".gif"))
            {
                //原始访问地址 D://upload_2021//shopsystem//
                //如果type是user 就上传到 D://upload_2021//shopsystem//user//image//
                uploadFileUrl = uploadFileUrl+"//"+type+"//image//";
                flag = "image";
            }else if(suffixName.equalsIgnoreCase(".mp4")){//视频只支持mp4 上传
                uploadFileUrl = uploadFileUrl+"//"+type+"//video//";
                flag = "image";
            }else {//其他文件类型 本例不支持
                System.out.println("不支持该类型文件");
                return "";
            }
            //文件保存地址 最后一定要添加 //
            File dest = new File(uploadFileUrl+fileName);//生成文件地址
            //目录不存在则创建
            if(!dest.getParentFile().exists()){
                dest.getParentFile().mkdirs();
            }
            try{
                file.transferTo(dest);
                //上传成功需要把该文件在服务器中实际访问地址返回
                String realUrl = request.getContextPath();
                String serverName = request.getServerName();
                int port = request.getServerPort();
                realUrl = "http://"+serverName+":"+port+realUrl+"/resources/"+type+"/"+flag+"/"+fileName;
                return realUrl;
            }catch (Exception e){
                System.out.println(e);
                return "";
            }
        }

    }

    public static String uploadMutiFiles(MultipartFile[] files, String uploadPath, HttpServletRequest request,String type){
        String itemImageOtherUrl = "";
        for(int i=0;i<files.length;i++){
            if(files[i].isEmpty()){
                System.out.println("当前位置:"+i+"无文件！");
                itemImageOtherUrl += " ";//如果当前无图片则直接添加 " "
            }else{
                String flag = "";
                boolean uploadFlag = false;
                String fileName= files[i].getOriginalFilename();
                System.out.println("原文件名:"+fileName);
                String suffixName = fileName.substring(fileName.lastIndexOf("."));//获取文件后缀
                //根据不同后缀 将不同类型文件上传到 不同文件夹（前端控制）
                System.out.println("文件后缀:"+suffixName);
                //文件名重构  考虑到文件同名的问题，这里最好将文件名通过 UUID 重新处理
                fileName = UUID.randomUUID()+suffixName;//这样可以保证生成的文件名 不重复
                System.out.println("重构后文件名:"+fileName);
                String uploadFileUrl = uploadPath;
                //实现  根据type 处理要上传文件的位置
                //如果是图片  后缀可以是.jpeg .jpg .png .gif   如果还有其他类型  自行补充
                if(suffixName.equalsIgnoreCase(".jpeg")||
                        suffixName.equalsIgnoreCase(".jpg")||
                        suffixName.equalsIgnoreCase(".png")||suffixName.equalsIgnoreCase(".gif")){
                    //原始访问地址:  D://upload_2021//project1//
                    // 如果type是user   就上传到 D://upload_2021//project1//user//image//
                    uploadFileUrl = uploadFileUrl+"//"+type+"//image//";
                    flag = "image";
                    uploadFlag = true;
                }else if(suffixName.equalsIgnoreCase(".mp4")){//视频只支持mp4 上传
                    uploadFileUrl = uploadFileUrl+"//"+type+"//video//";
                    flag = "video";
                    uploadFlag = true;
                }else{//其他文件类型处理   本例:不支持
                    System.out.println("不支持该类型文件");
                    uploadFlag = false;
                }
                if(uploadFlag){
                    //文件保存地址   最后一定要添加  //
                    File dest = new File(uploadFileUrl+fileName);//生成文件地址
                    //目录不存在则创建
                    if(!dest.getParentFile().exists()){
                        dest.getParentFile().mkdirs();
                    }
                    try {
                        files[i].transferTo(dest);
                        //上传成功需要把该文件在服务器中实际访问地址返回
                        String realUrl = request.getContextPath();
                        System.out.println("realUrl前缀:"+realUrl);
                        String serverName = request.getServerName();
                        int port = request.getServerPort();

                        //获取项目访问上传文件的实际地址
                        realUrl = "http://"+serverName+":"+port+realUrl+"/resources/"+type+"/"+flag+"/"+fileName;
                        itemImageOtherUrl = itemImageOtherUrl+realUrl+" ";//地址正确将地址拼接到其他图片中
                    }catch (Exception e){
                        System.out.println(e);
                    }
                }

            }
        }

        return itemImageOtherUrl;
    }

    public static String uploadMutiFileUpdate(MultipartFile[] files, String uploadPath, HttpServletRequest request,String type,String[] originalUrl){
        //将originalUrl处理 originalUrl地址根据顺序 只会顺序存储
        int size = originalUrl.length;
        System.out.println(size);//主义这里分割后的数据可能会包含[]需要去除
        String newOriginalUrl = "";
        for(int i=0;i<size;i++){
            if(0==i){
                newOriginalUrl = originalUrl[0].substring(1);
            }else if(i==size-1){
                newOriginalUrl+=" "+originalUrl[i].substring(0,originalUrl[i].length()-1);
            }else {
                newOriginalUrl+=" "+originalUrl[i];
            }
        }
        System.out.println(newOriginalUrl);//重构后的图片地址
        originalUrl = newOriginalUrl.split(" ");//重构成数组

        String itemImageOtherUrl = "";
        for(int i=0;i<files.length;i++){
            if(files[i].isEmpty()){
                System.out.println("当前位置:"+i+"无文件！");
                //由于originalUrl按序存储，其他位置如果没有图 不需要处理
                if(i<originalUrl.length){
                    itemImageOtherUrl += originalUrl[i]+" ";//如果当前无图片则直接添加 " "
                }
            }else{
                String flag = "";
                boolean uploadFlag = false;
                String fileName= files[i].getOriginalFilename();
                System.out.println("原文件名:"+fileName);
                String suffixName = fileName.substring(fileName.lastIndexOf("."));//获取文件后缀
                //根据不同后缀 将不同类型文件上传到 不同文件夹（前端控制）
                System.out.println("文件后缀:"+suffixName);
                //文件名重构  考虑到文件同名的问题，这里最好将文件名通过 UUID 重新处理
                fileName = UUID.randomUUID()+suffixName;//这样可以保证生成的文件名 不重复
                System.out.println("重构后文件名:"+fileName);
                String uploadFileUrl = uploadPath;
                //实现  根据type 处理要上传文件的位置
                //如果是图片  后缀可以是.jpeg .jpg .png .gif   如果还有其他类型  自行补充
                if(suffixName.equalsIgnoreCase(".jpeg")||
                        suffixName.equalsIgnoreCase(".jpg")||
                        suffixName.equalsIgnoreCase(".png")||suffixName.equalsIgnoreCase(".gif")){
                    //原始访问地址:  D://upload_2021//project1//
                    // 如果type是user   就上传到 D://upload_2021//project1//user//image//
                    uploadFileUrl = uploadFileUrl+"//"+type+"//image//";
                    flag = "image";
                    uploadFlag = true;
                }else if(suffixName.equalsIgnoreCase(".mp4")){//视频只支持mp4 上传
                    uploadFileUrl = uploadFileUrl+"//"+type+"//video//";
                    flag = "video";
                    uploadFlag = true;
                }else{//其他文件类型处理   本例:不支持
                    System.out.println("不支持该类型文件");
                    uploadFlag = false;
                }
                if(uploadFlag){
                    //文件保存地址   最后一定要添加  //
                    File dest = new File(uploadFileUrl+fileName);//生成文件地址
                    //目录不存在则创建
                    if(!dest.getParentFile().exists()){
                        dest.getParentFile().mkdirs();
                    }
                    try {
                        files[i].transferTo(dest);
                        //上传成功需要把该文件在服务器中实际访问地址返回
                        String realUrl = request.getContextPath();
                        System.out.println("realUrl前缀:"+realUrl);
                        String serverName = request.getServerName();
                        int port = request.getServerPort();

                        //获取项目访问上传文件的实际地址
                        realUrl = "http://"+serverName+":"+port+realUrl+"/resources/"+type+"/"+flag+"/"+fileName;
                        itemImageOtherUrl = itemImageOtherUrl+realUrl+" ";//地址正确将地址拼接到其他图片中
                    }catch (Exception e){
                        System.out.println(e);
                    }
                }

            }
        }

        return itemImageOtherUrl;
    }

    //更新商品时，上传单个文件功能
    public static String uploadOneFileUpdate(MultipartFile file, String uploadPath, HttpServletRequest request,String type,String originalUrl){
        String flag="";
        String newUrl="";
        if(file.isEmpty()){
            System.out.println("没有获取到文件");
            newUrl = originalUrl;//如果没有上传新文件地址，那么这里使用原始地址
        }else {
            String fileName = file.getOriginalFilename();
            System.out.println("文件名："+fileName);
            String suffixName = fileName.substring(fileName.lastIndexOf("."));//获取文件后缀
            //根据不同后缀 将不同类型文件上传到不同文件夹（前端控制）
            System.out.println("文件后缀："+suffixName);
            //文件保存地址 最后一定要添加//
            //文件名重构 考虑到文件同名的问题，这里最好将文件名通过UUID重新处理
            fileName = UUID.randomUUID()+suffixName;//这样可以保证生成的文件名 不重复
            System.out.println("重构后文件名："+fileName);
            String uploadFileUrl = uploadPath;
            //如果是图片后缀可以是.jpeg .jpg .gif .png 如果还有其他类型
            if(suffixName.equalsIgnoreCase(".jpg")||suffixName.equalsIgnoreCase(".png")
                    ||suffixName.equalsIgnoreCase(".jpeg")||suffixName.equalsIgnoreCase(".gif"))
            {
                //原始访问地址 D://upload_2021//shopsystem//
                //如果type是user 就上传到 D://upload_2021//shopsystem//user//image//
                uploadFileUrl = uploadFileUrl+"//"+type+"//image//";
                flag = "image";
            }else if(suffixName.equalsIgnoreCase(".mp4")){//视频只支持mp4 上传
                uploadFileUrl = uploadFileUrl+"//"+type+"//video//";
                flag = "image";
            }else {//其他文件类型 本例不支持
                System.out.println("不支持该类型文件");
                newUrl =  "";
            }
            //文件保存地址 最后一定要添加 //
            File dest = new File(uploadFileUrl+fileName);//生成文件地址
            //目录不存在则创建
            if(!dest.getParentFile().exists()){
                dest.getParentFile().mkdirs();
            }
            try{
                file.transferTo(dest);
                //上传成功需要把该文件在服务器中实际访问地址返回
                String realUrl = request.getContextPath();
                String serverName = request.getServerName();
                int port = request.getServerPort();
                realUrl = "http://"+serverName+":"+port+realUrl+"/resources/"+type+"/"+flag+"/"+fileName;
                newUrl = realUrl;
            }catch (Exception e){
                System.out.println(e);
                newUrl =  "";;
            }

        }
        return newUrl;
    }




}
