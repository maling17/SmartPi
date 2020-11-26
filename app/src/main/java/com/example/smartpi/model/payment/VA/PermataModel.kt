package com.example.smartpi.model.payment.VA

class PermataModel {

    fun getData(): HashMap<String, List<String>> {
        val expandableListDetail = HashMap<String, List<String>>()
        val atmPermata: MutableList<String> = ArrayList()

        atmPermata.add("1. Pilih menu utama, pilih transaksi lain.")
        atmPermata.add("2. Pilih Pembayaran Transfer.")
        atmPermata.add("3. Pilih Pembayaran Lainnya.")
        atmPermata.add("4. Pilih Pembayaran Virtual Account.")
        atmPermata.add("5. Masukaan nomor virtual account anda.")
        atmPermata.add("6. Pada halaman konfirmasi, akan muncul nominal yang dibayarkan, nomor, dan nama merchant, lanjutkan jika sudah sesuai.")
        atmPermata.add("7. Pilih sumber pembayaran anda dan lanjutkan.")
        atmPermata.add("8. Transaksi anda selesai.")
        atmPermata.add("9. Ketika transaksi anda sudah selesai. invoice anda akan diupdate secara otomatis. Ini mungkin memakan waktu hingga 5 menit.")

        val internetBanking: MutableList<String> = ArrayList()
        internetBanking.add("1. Buka Permata Mobile dan Login.")
        internetBanking.add("2. Pilih Pay \"Pay Bills/Pembayaran Tagihan\".")
        internetBanking.add("3. Pilih menu \"Transfer\".")
        internetBanking.add("4. Pilih sumber pembayaran.")
        internetBanking.add("5. Pilih \"daftar tagihan baru\".")
        internetBanking.add("6. Masukan nomor Virtual Account Anda.")
        internetBanking.add("7. Periksa ulang mengenai transaksi yang anda ingin lakukan.")
        internetBanking.add("8. Konfirmasi transaksi anda.")
        internetBanking.add("9. Masukan SMS token respons.")
        internetBanking.add("10. Pembayaran Anda berhasil.")
        internetBanking.add("11.Ketika transaksi anda sudah selesai. invoice anda akan diupdate secara otomatis. Ini mungkin memakan waktu hingga 5 menit.")

        val mobileBanking: MutableList<String> = ArrayList()
        mobileBanking.add("1. Buka situs https;//new.permatanet.com dan login.")
        mobileBanking.add("2. Pilih menu \"pembayaran\".")
        mobileBanking.add("3. Pilih \"Pembayaran Tagihan\".")
        mobileBanking.add("4. Pilih \"Virtual Account\".")
        mobileBanking.add("5. Pilih sumber pembayaran.")
        mobileBanking.add("6. Pilih menu \"Masukkan Daftar Tagihan Baru\".")
        mobileBanking.add("7. Masukan nomor Virtual Account Anda.")
        mobileBanking.add("8. Konfirmasi transaksi anda.")
        mobileBanking.add("9. Masukan SMS token response.")
        mobileBanking.add("10. Pembayaran Anda berhasil.")
        mobileBanking.add("11. Ketika transaksi anda sudah selesai, invoice anda akan diupdate secara otomatis. Ini mungkin memakan waktu hingga 5 menit.")

        val bankLain: MutableList<String> = ArrayList()
        bankLain.add("1. Pilih Menu Lain")
        bankLain.add("2. Pilih Transfer")
        bankLain.add("3. Pilih dari rekening tabungan")
        bankLain.add("4. Pilih ke rek. Bank lain")
        bankLain.add("5. Masukan kode bank dilanjutkan dengan nomor Virtual Account anda(BRI 013+XXXXXXXXXXXXXXX)")
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

        expandableListDetail["ATM Bank Permata"] = atmPermata
        expandableListDetail["Internet Banking Bank Permata"] = internetBanking
        expandableListDetail["Mobile Banking Bank Permata"] = mobileBanking
        expandableListDetail["ATM Bank Lain"] = bankLain
        expandableListDetail["Internet Banking Bank Lain"] = internetBankLain
        return expandableListDetail
    }
}
