--    PSHDL is a library and (trans-)compiler for PSHDL input. It generates
--    output suitable for implementation or simulation of it.
--
--    Copyright (C) 2013 Karsten Becker (feedback (at) pshdl (dot) org)
--
--    This program is free software: you can redistribute it and/or modify
--    it under the terms of the GNU General Public License as published by
--    the Free Software Foundation, either version 3 of the License, or
--    (at your option) any later version.
--
--    This program is distributed in the hope that it will be useful,
--    but WITHOUT ANY WARRANTY; without even the implied warranty of
--    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
--    GNU General Public License for more details.
--
--    You should have received a copy of the GNU General Public License
--    along with this program.  If not, see <http://www.gnu.org/licenses/>.
--
--    This License does not grant permission to use the trade names, trademarks,
--    service marks, or product names of the Licensor, except as required for
--    reasonable and customary use in describing the origin of the Work.
--
--Contributors:
--    Karsten Becker - initial API and implementation

--Updated for version 0.1.80 on 2014-05-19
--Added log2ceil and log2floor

--Updated for version 0.1.75 on 2014-04-23
--Added ternary overload for boolean
--Added not overload for integer

library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;

package ShiftOps is

    function srlUint(arg: unsigned; s:natural) return unsigned;
    function srlInt(arg: signed; s:natural) return signed;
    function srlNatural(arg: natural; s:natural) return natural;
    function srlInteger(arg: integer; s:natural) return integer;
    function srlBit(arg: std_logic; s:natural) return std_logic;
    function srlBitvector(arg: std_logic_vector; s:natural) return std_logic_vector;

    function sraUint(arg: unsigned; s:natural) return unsigned;
    function sraInt(arg: signed; s:natural) return signed;
    function sraNatural(arg: natural; s:natural) return natural;
    function sraInteger(arg: integer; s:natural) return integer;
    function sraBit(arg: std_logic; s:natural) return std_logic;
    function sraBitvector(arg: std_logic_vector; s:natural) return std_logic_vector;

    function sllUint(arg: unsigned; s:natural) return unsigned;
    function sllInt(arg: signed; s:natural) return signed;
    function sllNatural(arg: natural; s:natural) return natural;
    function sllInteger(arg: integer; s:natural) return integer;
    function sllBit(arg: std_logic; s:natural) return std_logic;
    function sllBitvector(arg: std_logic_vector; s:natural) return std_logic_vector;

end;

package body ShiftOps is
  function sraBitvector(arg: std_logic_vector; s:natural) return std_logic_vector is
  begin
    return std_logic_vector(sraUint(unsigned(arg),s));
  end sraBitvector;

  function srlBitvector(arg: std_logic_vector; s:natural) return std_logic_vector is
  begin
    return std_logic_vector(srlUint(unsigned(arg),s));
  end srlBitvector;

  function sllBitvector(arg: std_logic_vector; s:natural) return std_logic_vector is
  begin
    return std_logic_vector(sllUint(unsigned(arg),s));
  end sllBitvector;

  function sraBit(arg: std_logic; s:natural) return std_logic is
  begin
    --The MSB is the bit itself, so it is returned
    return arg;
  end sraBit;

  function srlBit(arg: std_logic; s:natural) return std_logic is
  begin
    if (s=0) then
        return arg;
    end if;
    return '0';
  end srlBit;

  function sllBit(arg: std_logic; s:natural) return std_logic is
  begin
    if (s=0) then
        return arg;
    end if;
    return '0';
  end sllBit;

  function srlUint(arg: unsigned; s:natural) return unsigned is
  begin
    return SHIFT_RIGHT(arg,s);
  end srlUint;

  function sraUint(arg: unsigned; s:natural) return unsigned is
  begin
    return SHIFT_RIGHT(arg,s);
  end sraUint;

  function sllUint(arg: unsigned; s:natural) return unsigned is
  begin
    return SHIFT_LEFT(arg,s);
  end sllUint;

  function srlInt(arg: signed; s:natural) return signed is
  begin
    return SIGNED(SHIFT_RIGHT(UNSIGNED(ARG), s));
  end srlInt;

  function sraInt(arg: signed; s:natural) return signed is
  begin
    return SHIFT_RIGHT(arg,s);
  end sraInt;

  function sllInt(arg: signed; s:natural) return signed is
  begin
    return SHIFT_LEFT(arg,s);
  end sllInt;

  function srlInteger(arg: integer; s:natural) return integer is
  begin
    return to_integer(SHIFT_RIGHT(to_UNSIGNED(ARG,32), s));
  end srlInteger;

  function sraInteger(arg: integer; s:natural) return integer is
  begin
    return to_integer(SHIFT_RIGHT(to_SIGNED(arg,32),s));
  end sraInteger;

  function sllInteger(arg: integer; s:natural) return integer is
  begin
    return to_integer(SHIFT_LEFT(to_SIGNED(arg,32),s));
  end sllInteger;

  function srlNatural(arg: natural; s:natural) return natural is
  begin
    return to_integer(SHIFT_RIGHT(to_UNSIGNED(ARG,32), s));
  end srlNatural;

  function sraNatural(arg: natural; s:natural) return natural is
  begin
    return to_integer(SHIFT_RIGHT(to_UNSIGNED(arg,32),s));
  end sraNatural;

  function sllNatural(arg: natural; s:natural) return natural is
  begin
    return to_integer(SHIFT_LEFT(to_UNSIGNED(arg,32),s));
  end sllNatural;
end;

library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;
package Casts is

    function max (LEFT, RIGHT: INTEGER) return INTEGER ;

    function min (LEFT, RIGHT: INTEGER) return INTEGER ;

    function boolToBit(ARG: boolean) return std_logic;
    function boolToBitvector(ARG: boolean) return std_logic_vector;
    function boolToInt(ARG: boolean) return signed;
    function boolToUint(ARG: boolean) return unsigned;
    function boolToInteger(ARG: boolean) return integer;
    function boolToNatural(ARG: boolean) return natural;

    function intToUint (ARG : signed) return unsigned;
    function intToBit (ARG : signed) return std_logic;
    function intToBitvector (ARG : signed) return std_logic_vector;
    function intToInteger (ARG : signed) return integer;
    function intToNatural (ARG : signed) return natural;
    function uintToInt (ARG : unsigned) return signed;
    function uintToBit (ARG : unsigned) return std_logic;
    function uintToBitvector (ARG : unsigned) return std_logic_vector;
    function uintToInteger (ARG : unsigned) return integer;
    function uintToNatural (ARG : unsigned) return natural;
    function bitToInt (ARG : std_logic) return signed;
    function bitToUint (ARG : std_logic) return unsigned;
    function bitToBitvector (ARG : std_logic) return std_logic_vector;
    function bitToInteger (ARG : std_logic) return integer;
    function bitToNatural (ARG : std_logic) return natural;

    function bitvectorToInt (ARG : std_logic_vector) return signed;
    function bitvectorToUint (ARG : std_logic_vector) return unsigned;
    function bitvectorToBit (ARG : std_logic_vector) return std_logic;
    function bitvectorToInteger (ARG : std_logic_vector) return integer;
    function bitvectorToNatural (ARG : std_logic_vector) return natural;

    function integerToInt (ARG : integer) return signed;
    function integerToUint (ARG : integer) return unsigned;
    function integerToBit (ARG : integer) return std_logic;
    function integerToBitvector (ARG : integer) return std_logic_vector;
    function integerToNatural (ARG : integer) return natural;
    function naturalToInt (ARG : natural) return signed;
    function naturalToUint (ARG : natural) return unsigned;
    function naturalToBit (ARG : natural) return std_logic;
    function naturalToBitvector (ARG : natural) return std_logic_vector;
    function naturalToInteger (ARG : natural) return integer;

    function resizeSLV(S:std_logic_vector; NEWSIZE:natural) return std_logic_vector;
    function resizeBit(S:std_logic; NEWSIZE:natural) return std_logic_vector;
    function resizeInt(S:signed; NEWSIZE:natural) return signed;
    function resizeUint(S:unsigned; NEWSIZE:natural) return unsigned;
    function resizeInteger(S:integer; NEWSIZE:natural) return signed;
    function resizeNatural(S:natural; NEWSIZE:natural) return unsigned;
end;

package body Casts is

  function MAX (LEFT, RIGHT: INTEGER) return INTEGER is
  begin
    if LEFT > RIGHT then return LEFT;
    else return RIGHT;
    end if;
  end MAX;

  function MIN (LEFT, RIGHT: INTEGER) return INTEGER is
  begin
    if LEFT < RIGHT then return LEFT;
    else return RIGHT;
    end if;
  end MIN;

    function boolToBit(ARG: boolean) return std_logic is
    begin
        if (ARG) then
            return '1';
        end if;
        return '0';
    end;

    function boolToBitvector(ARG: boolean) return std_logic_vector is
    begin
        if (ARG) then
            return "01";
        end if;
        return "00";
    end;

    function boolToInt(ARG: boolean) return signed is
    begin
        if (ARG) then
            return "01";
        end if;
        return "00";
    end;

    function boolToUint(ARG: boolean) return unsigned is
    begin
        if (ARG) then
            return "01";
        end if;
        return "00";
    end;

    function boolToInteger(ARG: boolean) return integer is
    begin
        if (ARG) then
            return 1;
        end if;
        return 0;
    end;

    function boolToNatural(ARG: boolean) return natural is
    begin
        if (ARG) then
            return 1;
        end if;
        return 0;
    end;

    function intToUint (ARG : signed) return unsigned is
    begin
        return unsigned(ARG);
    end;

    function intToBit (ARG : signed) return std_logic is
    begin
        if (ARG/=0) then
            return '1';
        else
            return '0';
        end if;
    end;

    function intToBitvector (ARG : signed) return std_logic_vector is
    begin
        return std_logic_vector(ARG);
    end;

    function intToInteger (ARG : signed) return integer is
    begin
        return to_integer(ARG);
    end;

    function uintToInt (ARG : unsigned) return signed is
    begin
        return signed(ARG);
    end;

    function uintToBit (ARG : unsigned) return std_logic is
    begin
        if (ARG/=0) then
            return '1';
        else
            return '0';
        end if;
    end;

    function uintToBitvector (ARG : unsigned) return std_logic_vector is
    begin
        return std_logic_vector(ARG);
    end;

    function uintToInteger (ARG : unsigned) return integer is
    begin
        return to_integer(signed(ARG));
    end;

    function bitToInt (ARG : std_logic) return signed is
    begin
        if (ARG/='0') then
            return to_signed(1,2);
        else
            return to_signed(0,2);
        end if;
    end;

    function bitToUint (ARG : std_logic) return unsigned is
    begin
        if (ARG/='0') then
            return to_unsigned(1,2);
        else
            return to_unsigned(0,2);
        end if;
    end;

    function bitToBitvector (ARG : std_logic) return std_logic_vector is

        variable res: std_logic_vector(0 downto 0);
    begin
        res(0):=ARG;
        return res;
    end;

    function bitToInteger (ARG : std_logic) return integer is
    begin
        if (ARG/='0') then
            return 1;
        else
            return 0;
        end if;
    end;

    function bitvectorToInt (ARG : std_logic_vector) return signed is
    begin
        return signed(ARG);
    end;

    function bitvectorToUint (ARG : std_logic_vector) return unsigned is
    begin
        return unsigned(ARG);
    end;

    function bitvectorToBit (ARG : std_logic_vector) return std_logic is
    begin
        if (ARG/=(ARG'range=>'0')) then
            return '1';
        else
            return '0';
        end if;
    end;

    function bitvectorToInteger (ARG : std_logic_vector) return integer is
    begin
        return to_integer(signed(ARG));
    end;

    function bitvectorToInt (ARG : signed) return signed is
    begin
        return ARG;
    end;

    function bitvectorToUint (ARG : signed) return unsigned is
    begin
        return unsigned(ARG);
    end;

    function bitvectorToBit (ARG : signed) return std_logic is
    begin
        if (ARG/=(ARG'range=>'0')) then
            return '1';
        else
            return '0';
        end if;
    end;

    function bitvectorToInteger (ARG : signed) return integer is
    begin
        return to_integer(signed(ARG));
    end;


    function bitvectorToInt (ARG : unsigned) return signed is
    begin
        return signed(ARG);
    end;

    function bitvectorToUint (ARG : unsigned) return unsigned is
    begin
        return ARG;
    end;

    function bitvectorToBit (ARG : unsigned) return std_logic is
    begin
        if (ARG/=(ARG'range=>'0')) then
            return '1';
        else
            return '0';
        end if;
    end;

    function bitvectorToInteger (ARG : unsigned) return integer is
    begin
        return to_integer(signed(ARG));
    end;


    function integerToInt (ARG : integer) return signed is
    begin
        return to_signed(ARG,32);
    end;

    function integerToUint (ARG : integer) return unsigned is
    begin
        return to_unsigned(ARG,32);
    end;

    function integerToBit (ARG : integer) return std_logic is
    begin
        if (ARG/=0) then
            return '1';
        else
            return '0';
        end if;
    end;

    function integerToBitvector (ARG : integer) return std_logic_vector is
    begin
        return std_logic_vector(to_signed(ARG,32));
    end;

    function intToNatural (ARG : signed) return natural is
    begin
        return to_integer(unsigned(ARG));
    end;

    function uintToNatural (ARG : unsigned) return natural is
    begin
        return to_integer('0'&ARG);
    end;

    function bitToNatural (ARG : std_logic) return natural is
    begin
        if (ARG/='0') then
            return 1;
        else
            return 0;
        end if;
    end;

    function bitvectorToNatural (ARG : std_logic_vector) return natural is
    begin
        return to_integer(unsigned(ARG));
    end;

    function bitvectorToNatural (ARG : unsigned) return natural is
    begin
        return to_integer(ARG);
    end;

    function bitvectorToNatural (ARG : signed) return natural is
    begin
        return to_integer(unsigned(ARG));
    end;

    function integerToNatural (ARG : integer) return natural is
    begin
        return ARG;
    end;

    function naturalToInt (ARG : natural) return signed is
    begin
        return to_signed(ARG,32);
    end;

    function naturalToUint (ARG : natural) return unsigned is
    begin
        return to_unsigned(ARG,32);
    end;

    function naturalToBit (ARG : natural) return std_logic is
    begin
        if (ARG/=0) then
            return '1';
        else
            return '0';
        end if;
    end;

    function naturalToBitvector (ARG : natural) return std_logic_vector is
    begin
        return std_logic_vector(to_unsigned(ARG,32));
    end;

    function naturalToInteger (ARG : natural) return integer is
    begin
        return ARG;
    end;

    function resizeSLV(S:std_logic_vector; NEWSIZE:natural) return std_logic_vector is
    begin
        return std_logic_vector(resize(unsigned(S),NEWSIZE));
    end;

    function resizeBit(S:std_logic; NEWSIZE:natural) return std_logic_vector is
    begin
        return std_logic_vector(resize(bitToUint(S),NEWSIZE));
    end;

    function resizeNatural(S:natural; NEWSIZE:natural) return unsigned is
    begin
        return resize(integerToUint(S),NEWSIZE);
    end;

    function resizeInteger(S:integer; NEWSIZE:natural) return signed is
    begin
        return resize(integerToInt(S),NEWSIZE);
    end;

    function resizeUint(S:unsigned; NEWSIZE:natural) return unsigned is
    begin
        if (S'length=NEWSIZE) then
            return S;
        elsif (S'LENGTH>NEWSIZE) then
            return S(NEWSIZE-1 downto 0);
        else
            return ((NEWSIZE-S'LENGTH)-1 downto 0 =>'0') & S;
        end if;
    end;

    function resizeInt(S:signed; NEWSIZE:natural) return signed is
    begin
        if (S'length=NEWSIZE) then
            return S;
        elsif (S'LENGTH>NEWSIZE) then
            return S(NEWSIZE-1 downto 0);
        else
            return ((NEWSIZE-S'LENGTH)-1 downto 0 =>S(S'LEFT)) & S;
        end if;
    end;
end;

library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;
use work.Casts.all;
use work.ShiftOps.all;
package Types is

    function ternaryOp(condition: boolean; thenValue: integer; elseValue:integer) return integer;
    function ternaryOp(condition: boolean; thenValue: boolean; elseValue:boolean) return boolean;
    function ternaryOp(condition: boolean; thenValue: unsigned; elseValue:unsigned) return unsigned;
    function ternaryOp(condition: boolean; thenValue: signed; elseValue:signed) return signed;
    function ternaryOp(condition: boolean; thenValue: std_logic_vector; elseValue:std_logic_vector) return std_logic_vector;
    function ternaryOp(condition: boolean; thenValue: std_logic; elseValue:std_logic) return std_logic;
    function "not" (L: INTEGER) return INTEGER;
    function log2ceil (L: POSITIVE) return NATURAL;
    function log2floor (L: POSITIVE) return NATURAL;
    function "or" (L: INTEGER; R: INTEGER) return INTEGER;
    function "xor" (L: INTEGER; R: INTEGER) return INTEGER;
    function "and" (L: INTEGER; R: INTEGER) return INTEGER;
    function widthOf(a: std_logic_vector) return INTEGER;
    function widthOf(a: unsigned) return INTEGER;
    function widthOf(a: signed) return INTEGER;
    function msbOf(a: std_logic_vector) return INTEGER;
    function msbOf(a: unsigned) return INTEGER;
    function msbOf(a: signed) return INTEGER;
end;

package body Types is

    function ternaryOp(condition: boolean; thenValue: boolean; elseValue:boolean) return boolean is
    begin
        if (condition) then
            return thenValue;
        else
            return elseValue;
        end if;
    end ternaryOp;

    function ternaryOp(condition: boolean; thenValue: integer; elseValue:integer) return integer is
    begin
        if (condition) then
            return thenValue;
        else
            return elseValue;
        end if;
    end ternaryOp;

    function ternaryOp(condition: boolean; thenValue: unsigned; elseValue:unsigned) return unsigned is
    begin
        if (condition) then
            return thenValue;
        else
            return elseValue;
        end if;
    end ternaryOp;

    function ternaryOp(condition: boolean; thenValue: signed; elseValue:signed) return signed is
    begin
        if (condition) then
            return thenValue;
        else
            return elseValue;
        end if;
    end ternaryOp;

    function ternaryOp(condition: boolean; thenValue: std_logic_vector; elseValue:std_logic_vector) return std_logic_vector is
    begin
        if (condition) then
            return thenValue;
        else
            return elseValue;
        end if;
    end ternaryOp;

    function ternaryOp(condition: boolean; thenValue: std_logic; elseValue:std_logic) return std_logic is
    begin
        if (condition) then
            return thenValue;
        else
            return elseValue;
        end if;
    end ternaryOp;

    function "not" (L: INTEGER) return INTEGER is
    begin
        return intToInteger(not resizeInteger(L,32));
    end "not";

    function log2ceil (L: POSITIVE) return NATURAL is
    variable bitCount : natural;
    variable i: unsigned(31 downto 0);
   begin
      i := to_UNSIGNED(L-1, 32);
        bitCount:=0;
      while (i > 0) loop
         bitCount := bitCount + 1;
            i:=SHIFT_RIGHT(i,1);
      end loop;
        return bitCount;
    end log2ceil;

    function log2floor (L: POSITIVE) return NATURAL is
    variable bitCount : natural;
    variable i: unsigned(31 downto 0);
   begin
      i := to_UNSIGNED(L,32);
        bitCount:=0;
      while (i > 1) loop
         bitCount := bitCount + 1;
            i:=SHIFT_RIGHT(i,1);
      end loop;
        return bitCount;
    end log2floor;


    function "or" (L: INTEGER; R: INTEGER) return INTEGER is
    begin
        return intToInteger(resizeInteger(L,32) or resizeInteger(R,32));
    end "or";

    function "xor" (L: INTEGER; R: INTEGER) return INTEGER is
    begin
        return intToInteger(resizeInteger(L,32) xor resizeInteger(R,32));
    end "xor";

    function "and" (L: INTEGER; R: INTEGER) return INTEGER is
    begin
        return intToInteger(resizeInteger(L,32) and resizeInteger(R,32));
    end "and";

    function widthOf(a: std_logic_vector) return INTEGER is
    begin
        return a'LENGTH;
    end widthOf;

    function widthOf(a: unsigned) return INTEGER is
    begin
        return a'LENGTH;
    end widthOf;

    function widthOf(a: signed) return INTEGER is
    begin
        return a'LENGTH;
    end widthOf;

    function msbOf(a: std_logic_vector) return INTEGER is
    begin
        return a'HIGH;
    end msbOf;

    function msbOf(a: unsigned) return INTEGER is
    begin
        return a'HIGH;
    end msbOf;

    function msbOf(a: signed) return INTEGER is
    begin
        return a'HIGH;
    end msbOf;

end;
