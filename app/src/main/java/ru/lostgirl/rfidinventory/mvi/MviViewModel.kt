package ru.lostgirl.rfidinventory.mvi

import android.app.Application
import androidx.annotation.CallSuper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharedFlow

open class MviViewModel<STATE, EFFECT, EVENT>(application: Application) :
    AndroidViewModel(application), ViewModelContract<EVENT> {

    private val _viewStates: MutableLiveData<STATE> = MutableLiveData()
    internal fun viewStates(): LiveData<STATE> = _viewStates

    private var _viewState: STATE? = null
    protected var viewState: STATE
        get() = _viewState
            ?: throw UninitializedPropertyAccessException("You must initialize viewState")
        set(value) {
            _viewState = value
            _viewStates.postValue(value)
        }

    private val _viewEffects: ChannelSharedFlow<EFFECT> = ChannelSharedFlow(viewModelScope)
    internal fun viewEffects(): SharedFlow<EFFECT> = _viewEffects

    private var _viewEffect: EFFECT? = null
    protected var viewEffect: EFFECT
        get() = _viewEffect
            ?: throw UninitializedPropertyAccessException("You must initialize viewEffect")
        set(value) {
            _viewEffect = value
            _viewEffects.tryEmit(value)
        }

    // Обработка событий поступающих из UI
    @CallSuper
    override fun process(viewEvent: EVENT) {
    }
}
