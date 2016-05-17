package com.example.gilang.latihan.dto;

import com.example.gilang.latihan.domain.Peserta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gilang on 13/05/16.
 */
public class PesertaResponse extends BaseEntity {

    private List<Peserta> content = new ArrayList<>();

    public List<Peserta> getContent() {
        return content;
    }

    public void setContent(List<Peserta> content) {
        this.content = content;
    }
}
