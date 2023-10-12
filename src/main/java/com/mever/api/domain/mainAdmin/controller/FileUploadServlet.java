package com.mever.api.domain.mainAdmin.controller;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

@WebServlet("/upload")
public class FileUploadServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        System.out.println("in@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@in@@@@@@@@@@@@@@@@@@@@@@@@@");

        if (isMultipart) {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);

            try {
                List<FileItem> items = upload.parseRequest(request);
                for (FileItem item : items) {
                    if (!item.isFormField()) {
                        String fileName = item.getName();
                        File uploadedFile = new File("C:\\fileUpload" + fileName);
                        item.write(uploadedFile);
                    }
                }
                response.getWriter().println("File uploaded successfully!");
            } catch (FileUploadException e) {
                e.printStackTrace();
                response.getWriter().println("File upload failed!");
            } catch (Exception e) {
                e.printStackTrace();
                response.getWriter().println("File upload failed!");
            }
        } else {
            response.getWriter().println("No file selected for upload!");
        }
    }
}
