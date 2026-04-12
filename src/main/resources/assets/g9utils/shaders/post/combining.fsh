#version 330

#define PI 3.1415926535

uniform sampler2D i0Sampler;
uniform sampler2D i1Sampler;
uniform sampler2D i2Sampler;
uniform sampler2D i3Sampler;
uniform sampler2D i4Sampler;

in vec2 texCoord;

layout(std140) uniform SamplerInfo {
    vec2 OutSize;
    vec2 i0Size;
    vec2 i1Size;
    vec2 i2Size;
    vec2 i3Size;
    vec2 i4Size;
};

out vec4 fragColor;

void main() {
    float pixelX = texCoord.x * OutSize.x;
    float leftEdge;
    if(pixelX > i0Size.x + i1Size.x + i2Size.x + i3Size.x) {
        fragColor = texture(i4Sampler, vec2((pixelX - i0Size.x - i1Size.x - i2Size.x - i3Size.x)/i4Size.x,texCoord.y));
    } else if(pixelX > i0Size.x + i1Size.x + i2Size.x) {
        fragColor = texture(i3Sampler, vec2((pixelX - i0Size.x - i1Size.x - i2Size.x)/i3Size.x,texCoord.y));
    } else if(pixelX > i0Size.x + i1Size.x) {
        fragColor = texture(i2Sampler, vec2((pixelX - i0Size.x - i1Size.x)/i2Size.x,texCoord.y));
    } else if(pixelX > i0Size.x) {
        fragColor = texture(i1Sampler, vec2((pixelX - i0Size.x)/i1Size.x,texCoord.y));
    } else {
        fragColor = texture(i0Sampler, vec2(pixelX/i0Size.x,texCoord.y));
    }
}