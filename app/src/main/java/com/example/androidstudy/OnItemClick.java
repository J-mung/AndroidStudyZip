package com.example.androidstudy;

// Adpater에서 클릭된 ViewHolder의 item info를 전달하기 위한 인터페이스
public interface OnItemClick {
    void onClick(String value) throws ClassNotFoundException;
}
