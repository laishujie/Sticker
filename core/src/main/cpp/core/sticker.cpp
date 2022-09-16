//
// Created by admin on 2022/9/6.
//

#include "sticker.h"
#include "shader/gl_program.h"

int Sticker::GetId() const {
    return id_;
}

void Sticker::SetId(int id) {
    id_ = id;
}

const std::string &Sticker::GetFilePath() const {
    return filePath_;
}

void Sticker::SetFilePath(const std::string &filePath) {
    filePath_ = filePath;
}

unsigned int Sticker::GetTextureId() const {
    return textureId_;
}

void Sticker::SetTextureId(unsigned int textureId) {
    textureId_ = textureId;
}

const Location &Sticker::GetLocation() const {
    return location_;
}

void Sticker::SetLocation(const Location &location) {
    location_ = location;
}

Sticker::Sticker() : id_(0), filePath_(), textureId_(0), location_(), rectF_(), glData_(nullptr),
                     isRest_(false) {

}

Sticker::~Sticker() {
    if (textureId_ != 0) {
        glDeleteTextures(1, &textureId_);
        textureId_ = 0;
    }
    if (glData_ != nullptr) {
        delete glData_;
        glData_ = nullptr;
    }
}

bool Sticker::IsRest() const {
    return isRest_;
}

void Sticker::SetIsRest(bool isRest) {
    isRest_ = isRest;
}

const RectF &Sticker::GetRectF() const {
    return rectF_;
}

void Sticker::SetRectF(const RectF &rectF) {
    rectF_ = rectF;
}

void Sticker::Rebuild(int w, int h) {
    if (isRest_) {
        if (glData_ == nullptr) {
            glData_ = new GlData();
            unsigned int index[] = {0, 1, 2, 2, 3, 0};

            GLfloat uv[] = {
                    0.0, 0.0,
                    1.0, 0.0,
                    1.0, 1.0,
                    0.0f, 1.0,
            };

            glData_ = new GlData();
            glData_->InitUvVbo2D(uv, 8, 1);
            glData_->InitEbo(index, 6);
        }
        float left = GLProgram::vertexWithPointX(rectF_.left_, w);
        float right = GLProgram::vertexWithPointX(rectF_.right_, w);
        float top = GLProgram::vertexWithPointY(rectF_.top_, h);
        float bottom = GLProgram::vertexWithPointY(rectF_.bottom_, h);

        GLfloat rectangleVertices[] = {
                left, top, 0.,
                right, top, 0.,
                right, bottom, 0.,
                left, bottom, 0.};
        glData_->UpdateVertexVbo(rectangleVertices, sizeof(rectangleVertices), 3);
    }

}

GlData *Sticker::GetGlData()  {
    return glData_;
}
