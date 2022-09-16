
//
// Created by admin on 2022/9/6.
//



#ifndef STICKER
#define STICKER

#include "util.hpp"
class GlData;
class Sticker {
public:
    Sticker();

    ~Sticker();

    int GetId() const;

    void SetId(int id);

    const std::string &GetFilePath() const;

    void SetFilePath(const std::string &filePath);

    unsigned int GetTextureId() const;

    void SetTextureId(unsigned int textureId);

    const Location &GetLocation() const;

    void SetLocation(const Location &location);

    bool IsRest() const;

    void SetIsRest(bool isRest);

    const RectF &GetRectF() const;

    void SetRectF(const RectF &rectF);

    void Rebuild(int w,int h);

private:
    int id_;
    std::string filePath_;
    unsigned int textureId_;
    Location location_;
    RectF rectF_;
    GlData *glData_;
public:
    GlData *GetGlData();

private:
    bool isRest_;
};

#endif //STICKER

