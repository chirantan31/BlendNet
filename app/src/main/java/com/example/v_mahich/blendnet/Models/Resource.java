package com.example.v_mahich.blendnet.Models;

import java.io.Serializable;

/**
 * Created by v-mahich on 9/6/2017.
 */

public class Resource implements Serializable {
    public int Id;
    public String fileName;
    public String link;
    public String createdAt;
    public String updatedAt;

    public String toString() {
        return fileName;
    }
}
