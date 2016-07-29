package com.buckethaendl.smartcart.data.local.file;

import java.io.File;

/**
 *
 * Created by Cedric on 20.07.16.
 */
public class FileLibrary {

    private File appDirectory;

    public static final String EKZ_FILE_NAME = "ekz.bin";

    private FileManager ekzFileManager;

    private static FileLibrary instance;

    public FileLibrary(File appDirectory) {

        this.appDirectory = appDirectory;
        FileLibrary.instance = this;

    }

    public static FileLibrary getInstance() {

        return FileLibrary.instance;

    }

    public void createManagers() {

        this.ekzFileManager = new FileManager(appDirectory, EKZ_FILE_NAME);

    }

    public FileInteractable getEKZFileManager() {
        return ekzFileManager;
    }

}
