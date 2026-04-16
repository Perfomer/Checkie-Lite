package com.perfomer.checkielite.common.ui.cui.widget.state

interface ViewState {
    interface Content : ViewState
    interface Loading : ViewState
    interface Error : ViewState
    interface Empty : ViewState
}