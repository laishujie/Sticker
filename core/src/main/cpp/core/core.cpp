//
// Created by admin on 2022/9/6.
//

#include "core.h"
#include "layer.h"
#include "sticker.h"
#include <GLES3/gl3.h>
#include "shader/all_shader.hpp"

int Core::glAddLayer(Layer *layer) {
    layerList_.emplace_back(layer);
    return 0;
}

int Core::glRemoverSticker(int layerId, int stickerId, bool isDraw) {
    Layer *pLayer = nullptr;
    int i = FindLayer(layerId, pLayer);
    if (i == 0) {
        std::vector<Sticker *> &list = pLayer->GetList();
        auto iterator = std::find_if(list.begin(), list.end(),
                                     [stickerId](Sticker *&sticker) {
                                         return sticker->GetId() == stickerId;
                                     });
        if (iterator != list.end()) {
            Sticker *&pSticker = *iterator;
            delete pSticker;
            pSticker = nullptr;
            list.erase(iterator);

            if (isDraw)
                glRendering();
            return 0;
        }
    }

    return -1;
}

int Core::FindLayer(int layerId, Layer *&pLayer) {
    auto iterator = std::find_if(layerList_.begin(), layerList_.end(),
                                 [layerId](Layer *&layer) { return layer->GetId() == layerId; });
    if (iterator != layerList_.end()) {
        pLayer = (*iterator);
        return 0;
    }
    return -1;
}

int Core::glRemoveLayer(int id, bool isDraw) {
    auto iterator = std::find_if(layerList_.begin(), layerList_.end(),
                                 [id](Layer *&layer) { return layer->GetId() == id; });

    int i = 0;
    if (iterator != layerList_.end()) {
        Layer *&pLayer = *iterator;
        delete pLayer;
        pLayer = nullptr;
        layerList_.erase(iterator);
        if (isDraw)
            glRendering();
    } else {
        i = -1;
    }
    return i;
}

int Core::glAddSticker(int layerId, Sticker *sticker) {
    if (sticker == nullptr) return 0;
    Layer *pLayer = nullptr;

    int findLayer = FindLayer(layerId, pLayer);

    if (findLayer == 0) {
        sticker->SetIsRest(true);
        pLayer->GetList().emplace_back(sticker);
        pLayer->SetIsUpdate(true);
    }
    return 0;
}


Core::Core() : layerList_(), surfaceWidth(0), surfaceHeight(0), imageShader_(nullptr) {

}

Core::~Core() {
    for (auto &layer:layerList_) {
        delete layer;
        layer = nullptr;
    }
    std::vector<Layer *>().swap(layerList_);
    layerList_.clear();
    if (imageShader_ != nullptr) {
        delete imageShader_;
        imageShader_ = nullptr;
    }
}

int Core::glRendering() {
    if (imageShader_ == nullptr) return 0;

    glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    glClear(GL_COLOR_BUFFER_BIT);

    for (auto &layer:layerList_) {
        layer->glInitLayer(surfaceWidth, surfaceHeight);
        //绘制贴纸
        bool isUpdate = layer->IsUpdate();
        if (isUpdate) {
            //切换图层缓冲区
            glBindFramebuffer(GL_FRAMEBUFFER, layer->GetFrameBuffer());
            glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            for (auto &sticker:layer->GetList()) {
                sticker->Rebuild(surfaceWidth, surfaceHeight);
                imageShader_->Render(sticker->GetTextureId(), sticker->GetGlData());
            }
        }

        //输出到屏幕
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        imageShader_->Render(layer->GetLayerTextureId());
    }

    return 0;
}

int Core::glInitShader(int sw, int sh) {
    surfaceWidth = sw;
    surfaceHeight = sh;
    imageShader_ = new ImageShader();
    return 0;
}
