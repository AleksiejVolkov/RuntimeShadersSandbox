# RuntimeShadersSandbox
This project serves as a sandbox for experimenting with runtime shader effects for jetpack compose.

## Overview
This project is a sandbox for testing Runtime Shaders written in Android's Shading Language (ALSL) within the Jetpack Compose framework. It is designed as a practical tool for developers to experiment with shader effects and their application in Android UI development, providing an environment for direct visual experimentation and understanding of shader integration in modern app design.

## First launch
Out of the box, the project includes a modest collection of shaders and a sample image for background testing. This pre-packaged content allows users to immediately start experimenting with different shader effects and see how they interact with common UI elements and backgrounds in Jetpack Compose

https://github.com/AleksiejVolkov/RuntimeShadersSandbox/assets/106402816/53ab0982-95b6-47b6-8a37-4a33a67b0b48

## How it works
Shaders are stored in the **assets/shaders** folder as text files. To display a new shader in the list, you first need to add the shader code as a separate file in this directory. Then, you must add an entry in the **assets/shaders_list.json** file with a reference to this new shader file. The structure of **shaders_list.json** is described in the following section, detailing how to properly link the shader files for them to be recognized and utilized within the application

### Shaders List JSON

The shaders_list.json file is a crucial part of the project, used for parsing the list of shaders and for defining the settings that can be modified by the user at runtime. Currently, it supports settings of types float and color. Here's an overview of its structure to guide users in adding new shaders:

Array of Shader Objects: The file consists of an array, where each object represents a shader.

Shader Object Structure:

`name:` A unique string identifier for the shader. Will be displayed on UI.    
`description:` A brief text describing what the shader does. Will be displayed on UI.   
`file:` The filename of the shader code, located in the assets/shaders directory. This links the JSON entry to the actual shader code.  
`properties:` An array of objects, each defining a customizable property for the shader.  

### Properties Object Structure:

`type:` Specifies the type of the property (now supported only: `float`, `color`).  
`name:` A unique name used to reference the property in the shader code.  
`displayName:` A human-readable name for the property, displayed in the UI.  
`defaultValue:` An array providing default values. The array should contain one value for float types, and three values (representing RGB components) for color types.  

### To add a new shader:

* Add the shader code as a `.txt` file in the `assets/shaders` folder.  
* Create a new object in the **shaders_list.json** array.  
* Fill in the name, description, and file fields. The file field should match the filename of the shader code you added.  
* Define the properties array according to the parameters your shader uses.  
*This structure allows the application to dynamically read the available shaders and their settings, offering flexibility and ease of expansion.  

## Example of Adding new Shader

in **assets/shaders** create a new .txt file with name `test_shader`. And copy paste the following code:  
```
//necessary for the shader to work:
uniform float2 resolution;
uniform shader image;
uniform float time;

//custom uniforms:

//end of custom uniforms

//helper functions:
vec2 normalizeUv(float2 fragCoord) {
    vec2 uv = fragCoord/resolution.xy - .5;
    return vec2(uv.x * resolution.x/resolution.y, uv.y);
}

half4 mergeImageAndShader(float2 fragCoord, vec4 col, vec4 background) {
    return half4(mix(background.rgb, col.rgb, col.a),col.a);
}
//end of helper functions

//beginning your of shader code:

//shader entry point:
half4 main(float2 fragCoord) {
    vec2 uv = normalizeUv(fragCoord); // get normalized uv coordinates

    float scaledTime = time * 0.01;
    vec4 col = vec4(0.,0.,0.,0.); // initialize the color to black with alpha 0

    return mergeImageAndShader(fragCoord, col, vec4(image.eval(fragCoord))); // return the final color
}
```

then in **shaders_list.json** add this shader just like that:  
```
...
 {
    "name": "My Test Shader",
    "description": "",
    "file": "test_shader",
    "properties": []
}
...
```
And that is it! 

### Shader requirements

To ensure the shaders function correctly within this application, there are specific requirements that each shader code must meet. While these are not standard requirements for all AGSL shaders, they are essential for compatibility with this app due to their frequent use. Each shader must declare the following three uniform variables:  

* `uniform float2 resolution;` - This variable is used to pass the resolution of the display or rendering surface to the shader.  
* `uniform shader image;` - Android's shader system, this uniform is not just a simple image; it represents another shader that contains information about what is currently rendered on the screen. This variable is particularly useful when you need to work with the alpha channel or overlay effects on the existing screen content.
* `uniform float time;` - This variable provides the shader with a time value, useful for creating animations or time-based effects.  
Even if your shader does not use these variables, they must still be declared to prevent the application from crashing.  
