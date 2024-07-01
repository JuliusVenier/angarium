import { writable } from "svelte/store";

export class Popup {
    constructor(text, color, timeAlive) {
        this.text = text;
        this.color = color;
        this.timeAlive = timeAlive;
        this.timeout();
    }

    static text;
    static color;
    static timeAlive;

    removeSelf() {
        removePopup(this);
    }

    async timeout() {
        if (this.timeAlive !== undefined && this.timeAlive !== null && !isNaN(this.timeAlive)) {
            setTimeout(() => {
                this.removeSelf(this);
            }, this.timeAlive);
        }
    }
}

export const popupColor = {
    accent: "accent",
    success: "success",
    warning: "warning",
    error: "error"
}

let localData = async () => {
    try {
        let data = localStorage.getItem("popups");
        let jsonArray = await JSON.parse(data);
        let array = [];
        jsonArray.forEach((item) => {
            array.push(new Popup(item.text, item.color, item.timeAlive));
        });
        return array;
    } catch (ex) {
        return [];
    }
};

export const popupArray = writable(await localData());
popupArray.subscribe((array) => {
            localStorage.setItem("popups", JSON.stringify(array));
    },
    (array) => {
        localStorage.clear();
    }
);

export function pushPopup(text, color, timeAvailable = 5000) {
    let popup = new Popup(text, color, timeAvailable);
    popupArray.update(array => [...array, popup]);
}

export function popPopup() {
    let array = popupArray;
    popupArray.update(array => {array.shift(); return array;});
}

function removePopup(popup) {
    popupArray.update(array => {
        let index = array.indexOf(popup);
        if (index > -1) {
            array.splice(index, 1);
        }
        return array;
    });
}