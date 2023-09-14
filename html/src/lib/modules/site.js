export default class Site {
    constructor(value) {
        this._value = value
    }

    set value(newValue) {
        this._value = newValue
    }

    get value() {
        return this._value
    }

    degrade(probability) {
        throw new Error("This method isn't implemented.")
    }
}
