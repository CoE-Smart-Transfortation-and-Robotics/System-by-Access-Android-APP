package com.telkom.ceostar.ui.recylerview

object ViewType {
    const val DEFAULT = 0
    const val LANGUAGE_TOGGLE = 1
}

data class MenuUser(val title: String, val iconLeft: Int, val iconRight: Int? = null, val viewType: Int = ViewType.DEFAULT, val onClick: (() -> Unit)? = null)

