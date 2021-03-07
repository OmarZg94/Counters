package com.mtg.counters.modules.fr_examples

import com.mtg.counters.Application
import com.mtg.counters.R

class ExamplesIterator() : Iterator {

    private val drinksExample = mutableListOf("Cups of coffee", "Glasses of water", "Some more")
    private val foodExample = mutableListOf("Hot-dogs", "Cupcakes eaten", "Chicken sandwich")
    private val miscExample = mutableListOf("Times sneezed", "Naps", "Day dreaming")

    override fun getDrinksAdapter(listener: ExampleClickListener): ExamplesAdapter = ExamplesAdapter(drinksExample, listener)

    override fun getFoodAdapter(listener: ExampleClickListener): ExamplesAdapter = ExamplesAdapter(foodExample, listener)

    override fun getMiscAdapter(listener: ExampleClickListener): ExamplesAdapter = ExamplesAdapter(miscExample, listener)

    override fun saveTemporalExample(name: String) = Application.getPreferences().saveData(R.string.sp_example, name)
}

private interface Iterator {
    fun getDrinksAdapter(listener: ExampleClickListener): ExamplesAdapter
    fun getFoodAdapter(listener: ExampleClickListener): ExamplesAdapter
    fun getMiscAdapter(listener: ExampleClickListener): ExamplesAdapter
    fun saveTemporalExample(name: String)
}