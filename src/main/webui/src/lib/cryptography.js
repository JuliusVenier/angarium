function convertStringToArrayBuffer(str) {
    let encoder = new TextEncoder("utf-8");
    return encoder.encode(str);
}
function convertArrayBufferToString(buffer) {
    let decoder = new TextDecoder("utf-8");
    return decoder.decode(buffer);
}

export function convertFileToArrayBuffer(file) {
    return new Promise((resolve, reject) => {
        file.arrayBuffer()
            .then(buffer => {
                resolve(buffer);
            })
            .catch(ex => {
                reject(ex);
            });
    });
}
function convertArrayBufferToFile(buffer, name, options) {
    return new Promise((resolve, reject) => {
        if (buffer === null || buffer === undefined) {
            throw new Error("read buffer is " + buffer);
        }
        try {
            let file = new File([buffer], name, options);
             return resolve(file);
        }
        catch (ex) {
            throw ex;
        }
    });
}

export function convertArrayBufferToHex(buffer) {
    return [...new Uint8Array(buffer)]
        .map(x => x.toString(16).padStart(2, '0'))
        .join('');
}

/**
 * Hashes an Arraybuffer with SHA-256
 * @param {ArrayBuffer} buffer
 * @returns {Promise<ArrayBuffer>}
 */
export function hashArrayBuffer(buffer) {
    return new Promise((resolve, reject) => {
        try {
            crypto.subtle.digest("SHA-256", buffer)
                .then(hash => {
                    return resolve(hash);
                })
                .catch(error => {
                    return reject(error);
                });
        }
        catch(error) {
            return reject(error);
        }
    });
}

/**
 * Hashes a Files Data
 * @param {File} file
 * @returns {Promise<ArrayBuffer>}
 */
export function hashFile(file) {
    return new Promise((resolve, reject) => {
        convertFileToArrayBuffer(file)
            .then(data => {
                hashArrayBuffer(data)
                    .then((buffer) => {
                        resolve(buffer);
                    })
                    .catch(error => {
                        reject(error);
                    });
            })
            .catch(ex => {
                reject(ex);
            });
    });
}

const iv = new Uint8Array([1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16]);

/**
 * Converts a password string to a CryptoKey
 * @param {string} pw
 * @returns {Promise<CryptoKey>}
 */
function generateKeyFromPassword(pw) {
    return new Promise(async (resolve, reject) => {
        try {
            let pwKey = await crypto.subtle.importKey(
                "raw",
                convertStringToArrayBuffer(pw),
                {name: "PBKDF2"},
                false,
                ["deriveKey"]
            );
            let key = await crypto.subtle.deriveKey(
                {
                    name: "PBKDF2",
                    salt: convertStringToArrayBuffer("true"),
                    iterations: 200,
                    hash: "SHA-256"
                },
                pwKey,
                {
                    name: "AES-CBC",
                    length: 256
                },
                false,
                ["encrypt", "decrypt"]
            );
            resolve(key);
        }
        catch(ex) {
            reject(ex);
        }
    });
}

/**
 * Encrypts an ArrayBuffer with a given Password
 * @param {ArrayBuffer} buffer
 * @param {string} pw
 * @returns {Promise<ArrayBuffer>}
 */
export function encryptArrayBuffer(buffer, pw) {
    return new Promise((resolve, reject) => {
        generateKeyFromPassword(pw)
            .then(key => {
                console.log("generateKeyFromPassword success", key);
                crypto.subtle.encrypt(
                    {name: "AES-CBC", iv: iv},
                    key,
                    buffer
                )
                    .then(encrypt => {
                        console.log("crypto.subtle.encrypt success", encrypt);
                        resolve(encrypt);
                    })
                    .catch(ex => {
                        console.log("crypto.subtle.encrypt error", ex);
                        reject(ex);
                    });
            })
            .catch(ex => {
                console.log("generateKeyFromPassword error", ex);
                reject(ex);
            });
    });
}

/**
 * Encrypts a File with a given Password
 * @param {File} file
 * @param {string} pw
 * @returns {Promise<File>}
 */
export function encryptFile(file, pw) {
    return new Promise((resolve, reject) => {
        convertFileToArrayBuffer(file)
            .then(buffer => {
                console.log("convertFileToArrayBuffer success", buffer);
                encryptArrayBuffer(buffer, pw)
                    .then(encryptedBuffer => {
                        console.log("encryptArrayBuffer success", encryptedBuffer);
                        convertArrayBufferToFile(encryptedBuffer, file.name, {type: file.type})
                            .then(file => {
                                console.log("convertArrayBufferToFile success", file);
                                resolve(file);
                            })
                            .catch(ex => {
                                console.log("convertArrayBufferToFile error", ex);
                                reject(ex);
                            })
                    })
                    .catch(ex => {
                        console.log("encryptArrayBuffer error", ex);
                        reject(ex);
                    });
            })
            .catch(ex => {
                console.log("convertFileToArrayBuffer error", ex);
                reject(ex);
            });
    });
}

/**
 * Decrypts an ArrayBuffer with a given Password
 * @param {ArrayBuffer} buffer
 * @param {string} pw
 * @returns {Promise<ArrayBuffer>}
 */
export function decryptArrayBuffer(buffer, pw) {
    return new Promise((resolve, reject) => {
        generateKeyFromPassword(pw)
            .then(key => {
                console.log("generateKeyFromPassword", key);
                try {
                    crypto.subtle.decrypt(
                        {name: "AES-CBC", iv: iv},
                        key,
                        buffer
                    )
                        .then(decrypt => {
                            console.log("decrypt", decrypt);
                            resolve(decrypt);
                        })
                        .catch(ex => {
                            console.log("decrypt error catch", ex);
                            reject(ex);
                        });
                }
                catch (ex) {
                    console.log("decrypt error", ex);
                }
            })
            .catch(ex => {
                reject(ex);
            });
    });
}

/**
 * Decrypts a File with a given Password
 * @param {File} file
 * @param {string} pw
 * @returns {Promise<File>}
 */
export function decryptFile(file, pw) {
    return new Promise((resolve, reject) => {
        convertFileToArrayBuffer(file)
            .then(buffer => {
                console.log("convertFileToArrayBuffer", buffer);
                decryptArrayBuffer(buffer, pw)
                    .then(decryptedBuffer => {
                        console.log("decryptArrayBuffer", decryptedBuffer);
                        convertArrayBufferToFile(decryptedBuffer, file.name, {type: file.type})
                            .then(decryptedFile => {
                                console.log("convertArrayBufferToFile", decryptedFile);
                                resolve(decryptedFile);
                            })
                            .catch(ex => {
                                reject(ex);
                            })
                    })
                    .catch(ex => {
                        reject(ex);
                    });
            })
            .catch(ex => {
                reject(ex);
            });
    });
}