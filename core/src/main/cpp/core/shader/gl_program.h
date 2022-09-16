//
// Created by admin on 2022/9/7.
//


#ifndef GL_PROGRAM
#define GL_PROGRAM

#include <GLES3/gl3.h>

enum GLShaderType {
    SHADER_VERTEX = 1,
    SHADER_FRAGMENT = 2
};

class GLShader {
public:
    GLShader(const char *shaderStr, GLShaderType type);

    ~GLShader();

    unsigned int shader_ = 0;
};

class GlData {
public:
    GlData();

    ~GlData();

    unsigned int vertexVbo_;
    unsigned int uvVbo_;
    unsigned int vao_;
    unsigned int ebo_;
    unsigned int elementIndex_;

    int InitVertexVbo(float *data, int vertexSize, int vertexGroupSize, GLuint layout = 0,
                      GLenum gLenum = GL_STATIC_DRAW);

    int InitUvVbo2D(float *data, int vertexSize, GLuint layout = 1, GLenum gLenum = GL_STATIC_DRAW);

    void
    UpdateUvVbo2D(float *data, int vertexSize, GLuint layout = 1, GLenum gLenum = GL_STATIC_DRAW);

    int InitEbo(unsigned int *indexData, int indexCount);

    void UpdateVertexVbo(float *data, int vertexSize, int vertexGroupSize, GLuint layout = 0,
                         GLenum gLenum = GL_STATIC_DRAW);

    void BindVAO() const;

    static void UnBindVAO();

    void DrawElement(GLenum mode = GL_TRIANGLES) const;
};


class GLProgram {
public:
    GLProgram(const char *vertexShaderStr, const char *fragmentShaderSrc);

    ~GLProgram();

    int UseProgram() const;

    unsigned int program_ = 0;


    static float vertexWithPointX(float x, int width) {
        return (x / float(width)) * 2.f - 1.f;
    }

    static float vertexWithPointY(float y, int height) {
        return 1.f - (y / float(height)) * 2.f;
    }
};

#endif //GL_PROGRAM