package com.example.smartpi.model.payment.VA

class DefaultBankModel {

    fun getData(): HashMap<String, List<String>> {
        val expandableListDetail = HashMap<String, List<String>>()

        val bankLain: MutableList<String> = ArrayList()
        bankLain.add("1. Pilih Menu Lain")
        bankLain.add("2. Pilih Transfer")
        bankLain.add("3. Pilih dari rekening tabungan")
        bankLain.add("4. Pilih ke rek. Bank lain")
        bankLain.add("5. Masukan kode bank dilanjutkan dengan nomor Virtual Account anda(BNI 009+xxxxxxxxxxxxxxx)")
        bankLain.add("6. Input Nominal yang ditagihkan sebagai Nominal Transfer")
        bankLain.add("7. Selesai, transaksi berhasil")

        val internetBankLain: MutableList<String> = ArrayList()
        internetBankLain.add("1. Masukan User ID dan Password")
        internetBankLain.add("2. Pilih Transfer")
        internetBankLain.add("3. Pilih dari rek. Bank Lain")
        internetBankLain.add("4. Pilih bank tujuan")
        internetBankLain.add("5. Masukan nomor Virtual Account anda(BNI 009+xxxxxxxxxxxxxxx)")
        internetBankLain.add("6. Input Nominal yang ditagihkan sebagai Nominal Transfer")
        internetBankLain.add("7. Selesai, transaksi berhasil")

        expandableListDetail["ATM Bank Lain"] = bankLain
        expandableListDetail["Internet Banking Bank Lain"] = internetBankLain

        return expandableListDetail
    }
}
