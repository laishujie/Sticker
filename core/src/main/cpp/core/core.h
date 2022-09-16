

//
// Created by admin on 2022/9/6.
//


#ifndef CORE_H
#define CORE_H

#include "vector"

class Layer;
class Sticker;
class ImageShader;
class Core {
private:
    std::vector<Layer*> layerList_;
    int surfaceWidth,surfaceHeight;
    ImageShader *imageShader_;
    int FindLayer(int layerId,Layer *&pLayer);
public:
    Core();
    ~Core();
    int glAddLayer(Layer *layer);
    int glRemoveLayer(int id, bool isDraw);
    int glAddSticker(int layerId,Sticker *sticker);
    int glRemoverSticker(int layerId, int stickerId, bool isDraw);
    int glRendering();
    int glInitShader(int sw,int sh);
};

#endif //CORE_H
