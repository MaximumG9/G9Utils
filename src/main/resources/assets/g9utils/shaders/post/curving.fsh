#version 330

#define PI 3.1415926535

uniform sampler2D InSampler;

in vec2 texCoord;

layout(std140) uniform SamplerInfo {
    vec2 OutSize;
    vec2 InSize;
};

layout(std140) uniform CurveConfig {
    float hFOV;
};

out vec4 fragColor;

const float near_plane_distance = 0.05f;

void main() {
    float near_plane_width = 2 * near_plane_distance * tan(hFOV / 2);
    float xAngle = (texCoord.x - 0.5) * hFOV;

    float newX = (((tan(xAngle) * near_plane_distance) / near_plane_width) + 0.5);
    float newY = ((texCoord.y - 0.5) / cos(xAngle)) * cos(hFOV/2) + 0.5;

    fragColor = texture(InSampler,vec2(newX,newY));
}