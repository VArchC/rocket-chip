package freechips.rocketchip.rocket

import chisel3._
import chisel3.util.HasBlackBoxResource

class mul8_303 extends BlackBox() with HasBlackBoxResource {
  val io = IO(new Bundle {
    val A = Input(UInt(8.W))
    val B = Input(UInt(8.W))
    val O = Output(UInt(16.W))
  })

  addResource("/vsrc/approx_mul.v")
}
object mul8_303 {
  def apply(mpcand: UInt, mplier: UInt) = {
    val m = Module(new mul8_303())
    m.io.A := mpcand
    m.io.B := mplier
    m.io.O
  }
}

class mul8_469 extends BlackBox() with HasBlackBoxResource {
  val io = IO(new Bundle {
    val A = Input(UInt(8.W))
    val B = Input(UInt(8.W))
    val O = Output(UInt(16.W))
  })

  addResource("/vsrc/approx_mul.v")
}
object mul8_469 {
  def apply(mpcand: UInt, mplier: UInt) = {
    val m = Module(new mul8_469())
    m.io.A := mpcand
    m.io.B := mplier
    m.io.O
  }
}

class mul8_479 extends BlackBox() with HasBlackBoxResource {
  val io = IO(new Bundle {
    val A = Input(UInt(8.W))
    val B = Input(UInt(8.W))
    val O = Output(UInt(16.W))
  })

  addResource("/vsrc/approx_mul.v")
}
object mul8_479 {
  def apply(mpcand: UInt, mplier: UInt) = {
    val m = Module(new mul8_479())
    m.io.A := mpcand
    m.io.B := mplier
    m.io.O
  }
}

class mul8_423 extends BlackBox() with HasBlackBoxResource {
  val io = IO(new Bundle {
    val A = Input(UInt(8.W))
    val B = Input(UInt(8.W))
    val O = Output(UInt(16.W))
  })

  addResource("/vsrc/approx_mul.v")
}
object mul8_423 {
  def apply(mpcand: UInt, mplier: UInt) = {
    val m = Module(new mul8_423())
    m.io.A := mpcand
    m.io.B := mplier
    m.io.O
  }
}

class mul8_279 extends BlackBox() with HasBlackBoxResource {
  val io = IO(new Bundle {
    val A = Input(UInt(8.W))
    val B = Input(UInt(8.W))
    val O = Output(UInt(16.W))
  })

  addResource("/vsrc/approx_mul.v")
}
object mul8_279 {
  def apply(mpcand: UInt, mplier: UInt) = {
    val m = Module(new mul8_279())
    m.io.A := mpcand
    m.io.B := mplier
    m.io.O
  }
}
