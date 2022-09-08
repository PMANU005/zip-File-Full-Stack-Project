package com.project.springbootzipproject;

import org.springframework.core.io.ByteArrayResource;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import java.net.URI;
import java.nio.file.Paths;
import org.springframework.http.HttpHeaders;
@RestController
@ResponseBody
@ResponseStatus(HttpStatus.ACCEPTED)

public class FileController {
    @CrossOrigin
    @PostMapping (value="/zipFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE ,produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
//    @PostMapping (value="/zipFile", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    private ResponseEntity<ByteArrayResource> fileConverter (
        @RequestParam("Incoming UnZip File")MultipartFile file
        ) throws Exception {
            File f=new File(file.getOriginalFilename()+".zip");
            FileOutputStream FoS = new FileOutputStream(f);
            ZipOutputStream zipOut = new ZipOutputStream(FoS);
            InputStream  FiS =file.getInputStream();
            ZipEntry zipEntry = new ZipEntry(file.getName());
            zipOut.putNextEntry(zipEntry);
            byte[] bytes = new byte[file.getBytes().length];
            int length;
            while ((length = FiS.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            zipOut.close();
            FiS.close();
            FoS.close();
            byte[] data=Files.readAllBytes(Paths.get(f.getPath()));
            ByteArrayResource resource=new ByteArrayResource(data);
            String headerKey="content-Disposition";
            String headerValue="attachment; fileName= "+"web.zip";
        return ResponseEntity.ok().header("Content-type","application/octet-stream").header(headerKey,headerValue).body(resource);
}
}
