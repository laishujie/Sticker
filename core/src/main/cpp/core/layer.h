
//
// Created by admin on 2022/9/6.
//


#ifndef LAYER_H
#define LAYER_H

class Sticker;

#include "vector"

class Layer {
public:
    Layer();

    ~Layer();

    int GetId() const;

    unsigned int GetFrameBuffer() const;

    unsigned int GetLayerTextureId() const;

    std::vector<Sticker *> &GetList();

    int glInitLayer(int sw,int sh);

private:
    int id_;
    unsigned int frameBuffer_;
    unsigned int layerTextureId_;
    bool isUpdate_;
public:
    bool IsUpdate() const;

    void SetIsUpdate(bool isUpdate);

private:
    std::vector<Sticker *> list_;
};

#endif //LAYER_H