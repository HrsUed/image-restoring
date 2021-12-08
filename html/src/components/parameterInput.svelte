<script>
    export let parameterValue
    export let step
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
    }
</style>
