export default class Site {
    private _value: number

    constructor(value: number) {
        this._value = value
    }

    set value(newValue: number) {
        this._value = newValue
    }

    get value() {
        return this._value
    }

    degrade(probability: number) {
        throw new Error("This method isn't implemented.")
    }
}
