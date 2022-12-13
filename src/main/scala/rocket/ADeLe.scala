package freechips.rocketchip.rocket

import Chisel._
import Chisel.ImplicitConversions._

class RKApproxes extends Bundle {
  val axram = Bool()
  val mul8_303 = Bool()
  val mul8_469 = Bool()
  val mul8_479 = Bool()
  val mul8_423 = Bool()
  val mul8_279 = Bool()
}

class RKRegs(val xLen: Int) extends Bundle {
  val mrkaddr = UInt(width = xLen)
  val mrkgroup = UInt(width = xLen)
  val mrkacbhv = UInt(width = xLen)
  val mrkdcbhv = UInt(width = xLen)
  val mrkav = UInt(width = xLen)
  val srkav = UInt(width = xLen)
  val urkav = UInt(width = xLen)
  val mrkst = UInt(width = xLen)
  val srkst = UInt(width = xLen)
  val urkst = UInt(width = xLen)
}

class RKCmd(val xLen: Int) extends Bundle {
  val wdata = UInt(width = xLen)
  val w_mrkgroup = Bool()
  val w_mrkacbhv = Bool()
  val w_mrkdcbhv = Bool()
  val w_srkav = Bool()
  val w_urkav = Bool()
  val w_mrkst = Bool()
  val w_srkst = Bool()
  val w_urkst = Bool()
  val prv = UInt(width = PRV.SZ)
}

class RKCSRIO(val xLen: Int) extends Bundle {
  val regs = new RKRegs(xLen).asInput
  val cmd = new RKCmd(xLen).asOutput
}

class RKControl(val xLen: Int) extends Module {
  val io = IO(new Bundle {
    val regs = new RKRegs(xLen).asOutput
    val cmd = new RKCmd(xLen).asInput
    val approxes = new RKApproxes().asOutput
  })

  val reset_regs = Wire(init=new RKRegs(xLen).fromBits(0))
  reset_regs.mrkav := "h3F".U(xLen.W)
  val regs = Reg(init=reset_regs)

  io.regs.mrkaddr := regs.mrkaddr
  io.regs.mrkgroup := regs.mrkgroup
  io.regs.mrkacbhv := regs.mrkacbhv
  io.regs.mrkdcbhv := regs.mrkdcbhv
  io.regs.mrkav := regs.mrkav
  io.regs.srkav := regs.srkav
  io.regs.urkav := regs.urkav
  io.regs.mrkst := regs.mrkst
  io.regs.srkst := (Fill(xLen, (io.cmd.prv===PRV.M))&regs.mrkav&regs.srkst) | (Fill(xLen, (io.cmd.prv===PRV.S))&regs.srkav&regs.srkst)
  io.regs.urkst := (Fill(xLen, (io.cmd.prv===PRV.M))&regs.mrkav&regs.urkst) | (Fill(xLen, (io.cmd.prv===PRV.S))&regs.srkav&regs.urkst) | (Fill(xLen, (io.cmd.prv===PRV.U))&regs.urkav&regs.urkst)
  
  val effective_st = (Fill(xLen, (io.cmd.prv===PRV.M))&regs.mrkst) | (Fill(xLen, (io.cmd.prv===PRV.S))&regs.srkst) | (Fill(xLen, (io.cmd.prv===PRV.U))&regs.urkst) & Fill(xLen, (regs.mrkgroup===0.U(xLen.W)))
  io.approxes.axram := effective_st(0)
  io.approxes.mul8_303 := effective_st(1)
  io.approxes.mul8_469 := effective_st(2)
  io.approxes.mul8_479 := effective_st(3)
  io.approxes.mul8_423 := effective_st(4)
  io.approxes.mul8_279 := effective_st(5)

  when(io.cmd.w_mrkgroup & (io.cmd.prv===PRV.M)) {
    regs.mrkgroup := io.cmd.wdata
    regs.mrkacbhv := 0.U(xLen.W)
    regs.mrkdcbhv := 0.U(xLen.W)
    regs.srkav := 0.U(xLen.W)
    regs.urkav := 0.U(xLen.W)
    regs.mrkst := 0.U(xLen.W)
    regs.srkst := 0.U(xLen.W)
    regs.urkst := 0.U(xLen.W)
  } .otherwise {
    when(io.cmd.w_mrkacbhv & (io.cmd.prv===PRV.M)) {
      regs.mrkacbhv := io.cmd.wdata
    }
    when(io.cmd.w_mrkdcbhv & (io.cmd.prv===PRV.M)) {
      regs.mrkdcbhv := io.cmd.wdata
    }

    when(io.cmd.w_srkav & (io.cmd.prv===PRV.M)) {
      regs.srkav := io.cmd.wdata & regs.mrkav
    }
    when(io.cmd.w_urkav & (io.cmd.prv===PRV.M)) {
      regs.urkav := io.cmd.wdata & regs.mrkav
    }
    when(io.cmd.w_urkav & (io.cmd.prv===PRV.S)) {
      regs.urkav := io.cmd.wdata & regs.srkav
    }

    when(io.cmd.w_mrkst & (io.cmd.prv===PRV.M)) {
      regs.mrkst := io.cmd.wdata & regs.mrkav
    }
    when(io.cmd.w_srkst & (io.cmd.prv===PRV.M)) {
      regs.srkst := io.cmd.wdata & regs.mrkav
    }
    when(io.cmd.w_srkst & (io.cmd.prv===PRV.S)) {
      regs.srkst := io.cmd.wdata & regs.srkav
    }
    when(io.cmd.w_urkst & (io.cmd.prv===PRV.M)) {
      regs.urkst := io.cmd.wdata & regs.mrkav
    }
    when(io.cmd.w_urkst & (io.cmd.prv===PRV.S)) {
      regs.urkst := io.cmd.wdata & regs.srkav
    }
    when(io.cmd.w_urkst & (io.cmd.prv===PRV.U)) {
      regs.urkst := io.cmd.wdata & regs.urkav
    }
  }

}
