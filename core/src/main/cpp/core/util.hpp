//
// Created by admin on 2022/9/6.
//

#ifndef STICKER_UTIL_HPP
#define STICKER_UTIL_HPP
#include "string"

class Location {
    float centerX_ = 0.f, centerY_ = 0.f, r_ = 0.f, s_ = 1.f;
};

class PointF {
    float x_ = 0.f, y_ = 0.f;
};

class RectF {
public:
    float left_ = 0.f;
    float top_ = 0.f;
    float right_ = 0.f;
    float bottom_ = 0.f;
};

#endif //STICKER_UTIL_HPP
