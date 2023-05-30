package com.example.posadvertise


enum class ActionType(val value : Int){
    NoAction(0),
    FORM(1001),
    SALE(1002),
    EMI_SALE(1003),
    BRAND_EMI(1004),
    BrandEMIByCode(1005),
    EMICatalogue(1006),
    SaleWithCash(1007),
    DigiPOS(1008),
    Void(1009),
    TipAdjust(1010),
    PreAuth(1011),
    CashAtPOS(1012),
    Refund(1013),
    PayLater(1014)
}