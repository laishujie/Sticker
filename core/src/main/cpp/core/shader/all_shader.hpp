//
// Created by admin on 2022/9/7.
//

#ifndef STICKER_ALL_SHADER_HPP
#define STICKER_ALL_SHADER_HPP

#include "gl_program.h"

#ifndef GLSL
#define GLSL(glsl) "#version 300 es \n precision mediump float; \n" #glsl;
#endif

class ILayerShader {
public:
    GLProgram *glProgram_;
    GlData *glDefaultData_;

    ILayerShader() : glProgram_(nullptr), glDefaultData_(nullptr) {
    }

    virtual ~ILayerShader() {
        if (glProgram_ != nullptr) {
            delete glProgram_;
            glProgram_ = nullptr;
        }
        if (glDefaultData_ != nullptr) {
            delete glDefaultData_;
            glDefaultData_ = nullptr;
        }
    }

    virtual void InitProgram(int sw, int sh) = 0;

    virtual void Render(unsigned int textureID, GlData *glData = nullptr) = 0;
};

class ImageShader : virtual public ILayerShader {
public:
    void InitProgram(int sw, int sh) override {
        if (glProgram_ != nullptr) return;
        const char *vertexShader = GLSL(
                uniform mat4 transform;
                layout(location = 0) in vec4 vPosition;
                layout(location = 1) in vec3 uvPos;
                out vec3 outUvPos;
                void main() {
                    vec2 vector2 = vec2(vPosition.x, vPosition.y);
                    gl_Position = transform * vec4(vector2, 0., 1.);
                    outUvPos = uvPos;
                }
        )

        const char *fragmentShader = GLSL(
                out vec4 fragColor;
                uniform sampler2D textureMap;
                in vec3 outUvPos;
                uniform float alpha;
                void main() {
                    vec2 uv = vec2(outUvPos.x, outUvPos.y);
                    vec4 mainColor = texture(textureMap, uv);
                    fragColor = mainColor;
                }
        )

        glProgram_ = new GLProgram(vertexShader, fragmentShader);
        glDefaultData_ = new GlData();
        unsigned int index[] = {0, 1, 2, 2, 3, 0};

        GLfloat uv[] = {
                0.0, 0.0,
                1.0, 0.0,
                1.0, 1.0,
                0.0f, 1.0,
        };

        GLfloat rectangleVertices[] = {
                -1.0f, 1.0f, 0.f,// 左上角
                1.0f, 1.0f, 0.f,//右上角
                1.0f, -1.0f, 0.f,//右下角
                -1.0f, -1.0f, 0.f};//左下角

        glDefaultData_->InitVertexVbo(rectangleVertices, 12, 3);
        glDefaultData_->InitUvVbo2D(uv, 8, 1);
        glDefaultData_->InitEbo(index, 6);
    }

    void Render(unsigned int textureID, GlData *glData = nullptr) override {
        if (glProgram_ == nullptr) return;

        glProgram_->UseProgram();
        GLint textureIndex = glGetUniformLocation(glProgram_->program_, "textureMap");
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureID);
        glUniform1i(textureIndex, 0);
        glData == nullptr ? glDefaultData_->DrawElement() : glData->DrawElement();
    }


};

#endif //STICKER_ALL_SHADER_HPP
