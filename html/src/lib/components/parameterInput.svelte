<script lang='ts'>
    export let parameterValue: number
    export let step: number
    export let disabled = false
</script>

<div class="parameter">
    <label>
        <slot />
        <input type="number" step={step} bind:value={parameterValue} disabled={disabled}>
    </label>
</div>

<style>
    .parameter input {
        width: 8rem;
        margin-left: 1rem;
    }
</style>
