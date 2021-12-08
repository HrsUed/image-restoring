import Site from './site'

export default class IsingSpin extends Site {
    static Up = 1
    static Down = -1

    constructor(spin = IsingSpin.createRandomValue()) {
        const value = IsingSpin.measure(spin)

        super(value)

        this._spin = value
    }

    get value() {
        return this.spin
    }

    set value(newValue) {
        this.spin = newValue
    }

    get spin() {
        return this._spin
    }

    set spin(newValue) {
        this._spin = newValue
    }

    isUp() {
        return this._spin > 0
    }

    degrade(probability) {
        this.flipWithRandom(probability)
    }

    flip() {
        this._spin *= -1
    }

    flipWithRandom(probability) {
        if (Math.random() < probability) {
            this.flip()
        }
    }

    static createRandomValue() {
        return Math.random() - 0.5
    }

    static measure(value) {
        return value >= 0 ? this.Up : this.Down
    }

    measure() {
        return this.value = IsingSpin.measure(this.value)
    }
}
