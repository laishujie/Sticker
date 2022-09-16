//
// Created by admin on 2022/9/6.
//

#include "layer.h"
#include "sticker.h"
#include <GLES3/gl3.h>

Layer::Layer() : id_(0), frameBuffer_(0), layerTextureId_(0), list_(), isUpdate_(false) {

}

Layer::~Layer() {
    if (frameBuffer_ != 0) {
        glDeleteFramebuffers(1, &frameBuffer_);
        frameBuffer_ = 0;
    }
    if (layerTextureId_ != 0) {
        glDeleteTextures(1, &layerTextureId_);
        layerTextureId_ = 0;
    }
    if (!list_.empty()) {
        for (auto &bean:list_) {
            delete bean;
        }
        std::vector<Sticker *>().swap(list_);
    }
}

int Layer::GetId() const {
    return id_;
}

unsigned int Layer::GetFrameBuffer() const {
    return frameBuffer_;
}

unsigned int Layer::GetLayerTextureId() const {
    return layerTextureId_;
}

std::vector<Sticker *> &Layer::GetList() {
    return list_;
}

bool Layer::IsUpdate() const {
    return isUpdate_;
}

void Layer::SetIsUpdate(bool isUpdate) {
    isUpdate_ = isUpdate;
}

int Layer::glInitLayer(int sw, int sh) {
    if (frameBuffer_ == 0) {
        glGenFramebuffers(1, &frameBuffer_);
        glActiveTexture(GL_TEXTURE0);
        glGenTextures(1, &layerTextureId_);
        glBindTexture(GL_TEXTURE_2D, layerTextureId_);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, sw, sh, 0, GL_RGBA,
                     GL_UNSIGNED_BYTE, nullptr);
        glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer_);
        //使用纹理作为附件
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, layerTextureId_, 0);

        glBindTexture(GL_TEXTURE_2D, 0);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }
    return 0;
}
