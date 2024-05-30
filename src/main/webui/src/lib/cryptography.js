function convertStringToArrayBuffer(str) {
    let encoder = new TextEncoder("utf-8");
    return encoder.encode(str);
}
function convertArrayBufferToString(buffer) {
    let decoder = new TextDecoder("utf-8");
    return decoder.decode(buffer);
}

function convertFileToArrayBuffer(file, callback) {
    if (file === null || file === undefined) {
        console.error("read file is " + file);
        return callback(null);
    }
    try {
        let reader = new FileReader();
        reader.onload = callback;
        reader.readAsArrayBuffer(file);
    }
    catch (exception) {
        console.error(exception);
        return callback(null);
    }
}
//TODO file download from decrypted buffer
/*function convertArrayBufferToFile(buffer, callback) {
    if (buffer === null || buffer === undefined) {
        console.error("read buffer is " + buffer);
        return callback(null);
    }
    try {
        const link = document.createElement('a');
        link.style.display = 'none';
        document.body.appendChild(link);

        const file = new Blob(buffer, {type: 'application/octet-stream'});
        const objectURL = URL.createObjectURL(file);

        link.href = objectURL;
        link.download = 'data.json';
        link.click();
    }
    catch (exception) {
        console.error(exception);
        return callback(null);
    }
}*/

export function convertArrayBufferToHex(buffer) {
    return [...new Uint8Array(buffer)]
        .map(x => x.toString(16).padStart(2, '0'))
        .join('');
}

export function hashFile(file) {
    return new Promise((resolve, reject) => {
        convertFileToArrayBuffer(file, (e) => {
            try {
                const data = e.target.result;
                if (data === null || data === undefined || data.byteLength === 0) {
                    throw new Error("invalid data");
                }

                crypto.subtle.digest("SHA-256", data)
                    .then(hash => {
                        resolve(hash);
                    })
                    .catch(error => {
                        console.error(error);
                        reject();
                    });
            }
            catch(error) {
                console.error(error);
                reject();
            }
        });
    });
}
export function hashArrayBuffer(buffer) {
    return new Promise((resolve, reject) => {
        try {
            const data = buffer;

            crypto.subtle.digest("SHA-256", data)
                .then(hash => {
                    resolve(hash);
                })
                .catch(error => {
                    console.error(error);
                    reject();
                });
        }
        catch(error) {
            console.error(error);
            reject();
        }
    });
}


//let iv = crypto.getRandomValues(new Uint8Array(16));
let iv = new Uint8Array([1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16]);

export function encryptFile(file, pw) {
    return new Promise((resolve, reject) => {
        convertFileToArrayBuffer(file, async (e) => {
            try {
                let data = e.target.result;
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

                let encrypt = await crypto.subtle.encrypt(
                    {name: "AES-CBC", iv: iv},
                    key,
                    data
                );

                resolve(encrypt);
            } catch (ex) {
                console.error(ex);
                reject();
            }
        });
    });
}

//TODO file decryption
export function decryptFile(encrypt, pw) {
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

            let decrypt = await crypto.subtle.decrypt(
                {name: "AES-CBC", iv: iv},
                key,
                encrypt
            );

            //convertArrayBufferToFile(decrypt);
            resolve(decrypt);
        } catch (ex) {
            console.error(ex);
            reject();
        }
    });
}