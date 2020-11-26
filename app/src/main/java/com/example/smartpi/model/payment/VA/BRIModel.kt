package com.example.smartpi.model.payment.VA

class BRIModel {

    fun getData(): HashMap<String, List<String>> {
        val expandableListDetail = HashMap<String, List<String>>()
        val atmBankBRI: MutableList<String> = ArrayList()

        atmBankBRI.add("1. Pilih menu Transaksi.")
        atmBankBRI.add("2. Pilih menu Lainnya. ")
        atmBankBRI.add("3. Menu pembayaran.")
        atmBankBRI.add("4. Pilih Menu Lainnya.")
        atmBankBRI.add("5. Pilih BRIVA.")
        atmBankBRI.add("6. Masukan Nomor BRI Virtual Account (Contoh: 26215-XXXXXXXXXXXXXXX), lalu tekan \"Benar\".")
        atmBankBRI.add("7. Konfirmasi pembayaran, tekan \"Ya\" bila selesai.")

        val internetBanking: MutableList<String> = ArrayList()
        internetBanking.add("1. Masukan User ID dan Password.")
        internetBanking.add("2. Pilih menu Pembayaran.")
        internetBanking.add("3. Pilih menu BRIVA.")
        internetBanking.add("4. Pilih rekening Pembayar.")
        internetBanking.add("5. Masukan Nomor BRI Virtual Account (Contoh: 26215-XXXXXXXXXXXXXXX).")
        internetBanking.add("6. Masukan nominal yang akan dibayar.")
        internetBanking.add("7. Masukan password dan Mtoken anda.")

        val mobileBanking: MutableList<String> = ArrayList()
        mobileBanking.add("1. Login in ke Mobile Banking.")
        mobileBanking.add("2. Pilih menu Pembayaran.")
        mobileBanking.add("3. Pilih menu BRIVA.")
        mobileBanking.add("4. Masukan BRI nomor Virtual Account dan jumlah pembayaran.")
        mobileBanking.add("5. Masukan nomor PIN anda.")
        mobileBanking.add("6. Tekan \"OK\" untuk melanjutkan transaksi.")
        mobileBanking.add("7. Transaksi berhasil.")
        mobileBanking.add("8. SMS konfirmasi akan masuk ke nomor telepon anda.")

        val bankLain: MutableList<String> = ArrayList()
        bankLain.add("1. Pilih Menu Lain")
        bankLain.add("2. Pilih Transfer")
        bankLain.add("3. Pilih dari rekening tabungan")
        bankLain.add("4. Pilih ke rek. Bank lain")
        bankLain.add("5. Masukan kode bank dilanjutkan dengan nomor Virtual Account anda(BRI 002+XXXXXXXXXXXXXXX)")
        bankLain.add("6. Input Nominal yang ditagihkan sebagai Nominal Transfer")
        bankLain.add("7. Selesai, transaksi berhasil")

        val internetBankLain: MutableList<String> = ArrayList()
        internetBankLain.add("1. Masukan User ID dan Password")
        internetBankLain.add("2. Pilih Transfer")
        internetBankLain.add("3. Pilih dari rek. Bank Lain")
        internetBankLain.add("4. Pilih bank tujuan")
        internetBankLain.add("5. Masukan nomor Virtual Account anda(BRI 009+xxxxxxxxxxxxxxx)")
        internetBankLain.add("6. Input Nominal yang ditagihkan sebagai Nominal Transfer")
        internetBankLain.add("7. Selesai, transaksi berhasil")

        expandableListDetail["ATM Bank Rakyat Indonesia"] = atmBankBRI
        expandableListDetail["Internet Banking Bank Rakyat Indonesia"] = internetBanking
        expandableListDetail["Mobile Banking Bank Rakyat Indonesia"] = mobileBanking
        expandableListDetail["ATM Bank Lain"] = bankLain
        expandableListDetail["Internet Banking Bank Lain"] = internetBankLain
        return expandableListDetail
    }
}
