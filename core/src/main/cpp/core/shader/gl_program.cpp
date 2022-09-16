//
// Created by admin on 2022/9/7.
//

#include "gl_program.h"
#include <GLES3/gl3.h>
#include <cstdlib>
#include <logUtil.h>

GLProgram::GLProgram(const char *vertexShaderStr, const char *fragmentShaderSrc) {
    program_ = glCreateProgram();
    GLShader vertexShader(vertexShaderStr, GLShaderType::SHADER_VERTEX);
    GLShader fragmentShader(fragmentShaderSrc, GLShaderType::SHADER_FRAGMENT);

    glAttachShader(program_, vertexShader.shader_);
    glAttachShader(program_, fragmentShader.shader_);

    glLinkProgram(program_);

    GLint logLen = 0;
    glGetProgramiv(program_, GL_INFO_LOG_LENGTH, &logLen);
    if (logLen > 0) {
        char *log = (char *) malloc(logLen);
        glGetProgramInfoLog(program_, logLen, nullptr, log);

        printf("Program Log:\n");
        printf("%s\n\n", log);

        LOGCATE("Program Log:\n")
        LOGCATE("%s", log)
        free(log);
    }
}

GLProgram::~GLProgram() {
    if (program_ != 0) {
        glDeleteProgram(program_);
        program_ = 0;
    }
}

int GLProgram::UseProgram() const {
    glUseProgram(program_);
    return 0;
}

GLShader::GLShader(const char *shaderStr, GLShaderType type) {
    if (type == GLShaderType::SHADER_VERTEX) {
        shader_ = glCreateShader(GL_VERTEX_SHADER);
    } else {
        shader_ = glCreateShader(GL_FRAGMENT_SHADER);
    }

    glShaderSource(shader_, 1, &shaderStr, nullptr);

    glCompileShader(shader_);

    GLint status;
    glGetShaderiv(shader_, GL_COMPILE_STATUS, &status);

    GLint logLen = 0;
    glGetShaderiv(shader_, GL_INFO_LOG_LENGTH, &logLen);
    if (logLen > 0) {
        char *log = (char *) malloc(logLen);
        glGetShaderInfoLog(shader_, logLen, nullptr, log);

        printf("Shader Log:\n");
        printf("%s\n", log);
        LOGCATE("Shader Log:\n")
        LOGCATE("%s", log)
        free(log);
    }
}

GLShader::~GLShader() {
    if (shader_ != 0) {
        glDeleteShader(shader_);
        shader_ = 0;
    }
}

GlData::GlData() : ebo_(0), vertexVbo_(0), uvVbo_(0), vao_(0), elementIndex_(0) {
    glGenVertexArrays(1, &vao_);
}

GlData::~GlData() {
    if (ebo_ != 0) {
        glDeleteBuffers(1, &ebo_);
        ebo_ = 0;
    }

    if (uvVbo_ != 0) {
        glDeleteBuffers(1, &uvVbo_);
        uvVbo_ = 0;
    }

    if (vertexVbo_ != 0) {
        glDeleteBuffers(1, &vertexVbo_);
        vertexVbo_ = 0;
    }

    if (vao_ != 0) {
        glDeleteVertexArrays(1, &vao_);
        vao_ = 0;
    }

}

int GlData::InitVertexVbo(float *data, int vertexSize, int vertexGroupSize, GLuint layout,
                          GLenum gLenum) {
    BindVAO();
    glGenBuffers(1, &vertexVbo_);
    glBindBuffer(GL_ARRAY_BUFFER, vertexVbo_);
    glBufferData(GL_ARRAY_BUFFER, vertexSize * sizeof(float), data, gLenum);
    glEnableVertexAttribArray(layout);
    glVertexAttribPointer(layout, vertexGroupSize, GL_FLOAT, GL_FALSE,
                          vertexGroupSize * sizeof(float), nullptr);

    glBindBuffer(GL_ARRAY_BUFFER, GL_NONE);
    UnBindVAO();
    return 0;
}

void GlData::UnBindVAO() {
    glBindVertexArray(0);
}

int GlData::InitUvVbo2D(float *data, int vertexSize, GLuint layout, GLenum gLenum) {
    BindVAO();
    glGenBuffers(1, &uvVbo_);
    glBindBuffer(GL_ARRAY_BUFFER, uvVbo_);
    glBufferData(GL_ARRAY_BUFFER, vertexSize * sizeof(float), data, gLenum);
    glEnableVertexAttribArray(layout);
    glVertexAttribPointer(layout, 2, GL_FLOAT, GL_FALSE, 2 * sizeof(float), nullptr);
    glBindBuffer(GL_ARRAY_BUFFER, GL_NONE);
    UnBindVAO();
    return 0;
}

int GlData::InitEbo(unsigned int *indexData, int indexCount) {
    BindVAO();
    glGenBuffers(1, &ebo_);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo_);
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexCount * sizeof(unsigned int), indexData,
                 GL_DYNAMIC_DRAW);
    elementIndex_ = indexCount;
    UnBindVAO();
    return 0;
}

void GlData::UpdateVertexVbo(float *data, int vertexSize, int vertexGroupSize, GLuint layout,
                             GLenum gLenum) {
    if (vertexVbo_ != 0) {
        BindVAO();
        glBindBuffer(GL_ARRAY_BUFFER, vertexVbo_);
        glBufferData(GL_ARRAY_BUFFER, vertexSize * sizeof(float), data, gLenum);
        glVertexAttribPointer(layout, vertexGroupSize, GL_FLOAT, GL_FALSE,
                              vertexGroupSize * sizeof(float), nullptr);
        glBindBuffer(GL_ARRAY_BUFFER, GL_NONE);
        UnBindVAO();
    } else {
        InitVertexVbo(data, vertexSize, vertexGroupSize, layout, gLenum);
    }
}

void GlData::UpdateUvVbo2D(float *data, int vertexSize, GLuint layout, GLenum gLenum) {
    if (uvVbo_ != 0) {
        BindVAO();
        glBindBuffer(GL_ARRAY_BUFFER, uvVbo_);
        glBufferData(GL_ARRAY_BUFFER, vertexSize * 2 * sizeof(float), data, gLenum);
        glVertexAttribPointer(layout, 2, GL_FLOAT, GL_FALSE, 2 * sizeof(float), nullptr);
        UnBindVAO();
        glBindBuffer(GL_ARRAY_BUFFER, GL_NONE);
    } else {
        InitUvVbo2D(data, vertexSize, layout, gLenum);
    }
}

void GlData::BindVAO() const {
    glBindVertexArray(vao_);
}

void GlData::DrawElement(GLenum mode) const {
    BindVAO();
    glDrawElements(mode, elementIndex_, GL_UNSIGNED_INT, nullptr);
    UnBindVAO();
}
