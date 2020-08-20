package bulat.ru.autofiscalization.utils

import androidx.lifecycle.MutableLiveData
import androidx.databinding.BindingAdapter
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("mutableVisibility")
fun setMutableVisibility(view: View, visibility: MutableLiveData<Int>) {
    visibility.value?.let {
        if (view.visibility != it) {
            view.visibility = it
        }
    }
}

@BindingAdapter("adapter")
fun setAdapter(view: RecyclerView, adapter: RecyclerView.Adapter<*>) {
    view.adapter = adapter
}

@BindingAdapter("mutableText")
fun setMutableText(textView: EditText, text: MutableLiveData<String>) {
    text.value?.let {
        if (textView.text.toString() != it)
            textView.setText(it)
    }
}

@BindingAdapter("mutableText")
fun setMutableText(textView: TextView, text: MutableLiveData<String>) {
    text.value?.let {
        if (textView.text.toString() != it)
            textView.setText(it)
    }
}

@BindingAdapter("load_image")
fun setSrc(imageView: ImageView, resource: MutableLiveData<Int>) {
    resource.value?.let {
        imageView.setImageResource(it)
    }
}

@BindingAdapter("android:onLongClick")
fun setOnLongClickListener(
    view: View,
    func: () -> Unit
) {
    view.setOnLongClickListener {
        func()
        true
    }
}