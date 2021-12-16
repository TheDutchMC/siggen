# siggen
Java signature generator

## Motivation
This program exists to extract all classes and their methods from a JAR file and throw them into a JSON document.
This JSON document can then be used to automatically generate JNI wrappers e.g.

`siggen` is part of my efforts to write an automatic toolchain that creates JNI wrapper code in Rust: `java2rs`

## Installation
Requirements:
- JDK 11

```
git clone https://github.com/TheDutchMC/siggen
cd siggen
./gradlew installDist
cd app/build/install/app/bin
```
You'll find the application as `app`

## Usage
```
./app 
    -jarfile <absolute path to the JAR file to export> 
    -output <absolute path to the output directory> 
    -rootPackage <The root package to export, e.g dev.array21> 
    [-dependencies <comma seperated list of absolute paths to runtime jars>]
```
You'll find the result as `output.json` in the output directory.
The arguments `-jarfile`, `-output` and `-rootPackage` are required. `-dependencies` is not.

## Output format
```jsonc
{
    "packages": [
        "string"
    ],
    "classes": [
        {
            "name": "string",
            "methods": [
                {
                    "returnType": {
                        "type": "string",
                        "isArray": false,
                    },
                    "isStatic": false,
                    "parameters": [
                        {
                            "name": "string",
                            "type": "string",
                            "isArray": false
                        }
                    ],
                    "fromInterface": "string" // Optional
                }
            ],
            "implementing": [
                "string"
            ]
        }
    ],
    "interfaces": [
        // Same definiton as classes
    ]
}
```

## License
`siggen` is licensed under the MIT license